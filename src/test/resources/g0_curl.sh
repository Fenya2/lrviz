curl -X POST http://localhost:8080/build/lr0 \
  -H "Content-Type: application/json" \
  -d '{
    "terminals": ["i", "r", "a", ";"],
    "nonTerminals": ["D", "T", "L"],
    "rules": [
      { "left": "D", "right": "TL" },
      { "left": "T", "right": "i" },
      { "left": "T", "right": "r" },
      { "left": "L", "right": "L;a" },
      { "left": "L", "right": "a" }
    ],
    "startSymbol": "D"
  }' | jq
