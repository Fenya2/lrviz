curl -X POST http://localhost:8080/api/build/lr0 \
  -H "Content-Type: application/json" \
    -d @grammars/api/g2.json | jq
