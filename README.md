# DonutSMP AutoReconnect

> **Meteor Client addon** — DonutSMP sunucusunda Y 0 ile -5 arasına indiğinde otomatik olarak çıkar ve yeniden bağlanır.

## Desteklenen Sürümler

| Klasör | Minecraft | Meteor |
|--------|-----------|--------|
| `mc-1.21.4/` | **1.21.4** | 1.21.4-SNAPSHOT |
| `mc-1.21.11/` | **1.21.11** | 26.1.2-SNAPSHOT |

## Kurulum

1. [Meteor Client](https://meteorclient.com)'ı indir ve kur (Fabric mod'u)
2. [Releases](../../releases) sayfasından MC sürümüne uygun JAR'ı indir
3. `.minecraft/mods/` klasörüne kopyala
4. Minecraft'ı başlat

## Kullanım

1. DonutSMP sunucusuna bağlan
2. **Right Shift** ile Meteor menüsünü aç
3. **Modules → DonutSMP → `donut-auto-reconnect`** modülünü aktif et (yeşil)
4. Hazır! Y 0 ile -5 arasına indiğinde:
   - Sunucudan otomatik çıkar
   - 3 saniye bekler
   - Otomatik geri bağlanır

## Nasıl Çalışır

Her tick'te oyuncunun Y koordinatı kontrol edilir. `donutsmp` içeren bir sunucuya bağlıysa ve Y koordinatı `-5.0 ≤ Y ≤ 0.0` aralığına girerse:

1. Sunucu adresi kaydedilir
2. `mc.disconnect()` çağrılır
3. 3 saniye (60 tick) beklenir
4. `ConnectScreen.connect()` ile aynı adrese bağlanılır

## Kendin Derle

Java 21+ ve Git gereklidir. GitHub Actions otomatik olarak her push'ta JAR üretir.

```bash
git clone https://github.com/afganpoco-commits/donutsmp-autodisconnect
cd donutsmp-autodisconnect/mc-1.21.4

# Linux/Mac
./gradlew build

# Windows
gradlew.bat build

# JAR: build/libs/donutsmp-autodisconnect-1.21.4-1.0.0.jar
```

## Lisans

MIT
