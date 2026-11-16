# TACZ Refabricated 1.20.1 -> 1.21.4 : status

## Sudah dikerjakan otomatis
- Java 17 -> 21 (build.gradle, mixin configs)
- Mojang mappings saja (Parchment 1.20.1 dibuang)
- Dependensi dibersihkan ke yang minimal (fabric-api, simplebedrockmodel, CCA, Forge Config API Port)
- 125 file: new ResourceLocation(..) -> ResourceLocation.fromNamespaceAndPath/parse
- Data pack: recipes->recipe, loot_tables->loot_table, tags/items->tags/item, dst; forge: -> c:
- 14 recipe vanilla -> format 1.21.2+ (ingredient string, result.id)
- assets/tacz/items/*.json (item model definitions 1.21.4) untuk 18 item

## BELUM (butuh iterasi compile)
- Jaringan: FriendlyByteBuf -> CustomPacketPayload + StreamCodec (60 file)
- NBT item -> Data Components (19 file)
- Rendering: BufferBuilder/Tesselator/ShaderInstance/RenderTarget/GuiGraphics
- 65 mixin harus dicek satu per satu
- Compat (JEI/REI/Iris/KubeJS/Cloth/Carry On/...) dependensinya dibuang; kodenya masih ada -> error compile
- ResourceLocation.Serializer (CommonAssetsManager, ClientAssetsManager) sudah tidak ada
- recipe tacz:* custom serializer, data/tacz/recipe/misc/*
- versi cca_version / fcap_version / fabric_version belum diverifikasi
