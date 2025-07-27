import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Counter } from 'k6/metrics';

export let ErrorRate   = new Counter('errors_total');
export let Latency     = new Trend('latency_ms');

export let options = {
  stages: [
    { duration: '30s', target:  50 },  // warm-up
    { duration: '2m',  target: 200 },  // steady load
    { duration: '30s', target:   0 },  // ramp-down
  ],
  // push VU metrics to Prometheus (collector ⇐ remote-write)
  ext: {
    loadimpact: {  // required dummy for xk6-prom-remote
      projectID: 1, name: "saga-checkout"
    }
  }
};

const BASE = 'http://order-svc:8080';   // or order-svc directly

export default function () {
  const payload = JSON.stringify({
    sku: 'SKU-42',
    qty: Math.random() < 0.1 ? 9 : 1,  // 10 % force inventory failure
    amount: 999.0
  });

  const params = { headers: { 'Content-Type': 'application/json' } };

  const res = http.post(`${BASE}/orders`, payload, params);

  const ok = check(res, { 'accepted': r => r.status === 202 });
  if (!ok) ErrorRate.add(1);

  Latency.add(res.timings.duration);

  sleep(0.5);             // ~2 RPS per VU at steady state
}
