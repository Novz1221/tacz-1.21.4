#!/bin/bash
# Dumps real 1.21.4 class signatures (mojmap names) so the port can be fixed against ground truth.
OUT=api-signatures.txt
: > "$OUT"
CP=$(find "$HOME/.gradle" /home/runner/work -name '*.jar' \( -path '*minecraftMaven*' -o -path '*remapped_mods*' -o -path '*modules-2*' \) 2>/dev/null | tr '\n' ':')
echo "classpath jars: $(echo "$CP" | tr ':' '\n' | grep -c .)" >> "$OUT"
MCJAR=$(find "$HOME/.gradle" -name '*.jar' -path '*minecraftMaven*' 2>/dev/null | xargs ls -S 2>/dev/null | head -1)
echo "minecraft jar: $MCJAR" >> "$OUT"
while read -r cls; do
  [ -z "$cls" ] && continue
  echo "" >> "$OUT"; echo "=== $cls" >> "$OUT"
  javap -p -cp "$CP" "$cls" 2>&1 | grep -v "^Warning" | head -400 >> "$OUT"
done < scripts/api-classes.txt
if [ -n "$MCJAR" ]; then
  for pkg in net/minecraft/client/renderer/special net/minecraft/client/renderer/item net/minecraft/advancements/critereon net/minecraft/world/item/crafting net/minecraft/client/gui/screens/inventory/tooltip; do
    echo "" >> "$OUT"; echo "=== classes in $pkg" >> "$OUT"
    unzip -Z1 "$MCJAR" | grep "^$pkg/[^/]*\.class$" | sed 's/\.class$//' | head -150 >> "$OUT"
  done
fi
wc -l "$OUT"
