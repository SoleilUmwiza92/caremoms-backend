import http from 'k6/http';
import { check, sleep } from 'k6';

// Define test options
export let options = {
  stages: [
    { duration: '30s', target: 50 }, // Ramp up to 50 users in 30s
    { duration: '1m', target: 50 },  // Stay at 50 users for 1m
    { duration: '30s', target: 0 }   // Ramp down to 0 users
  ]
};

export default function () {
  let data={roomId:'2',content:'Bonjour mon amis',receiverId:'2'}
  let res = http.post('http://localhost:8080/api/messages', JSON.stringify(data), {
                                                                headers: { 'Content-Type': 'application/json', 'Admin': 'joe.doe@gmail.com'}});
  check(res, {
    'response status is 200': (r) => r.status === 200,
    'response time is < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(1);
}