curl -X POST "http://localhost:8080/api/v1/build/lr0" \
  -H "Content-Type: application/json" \
  -H "Accept: image/png" \
  -d @grammars/api/g2.json \
  --output ./result.png