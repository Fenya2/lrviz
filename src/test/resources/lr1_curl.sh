curl -X POST http://localhost:8080/api/build/lr1 \
  -H "Content-Type: application/json" \
    -d @grammars/api/g8.json | jq | wc
