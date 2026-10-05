"""Baixa o overworld.json original do Minecraft 1.21.1 e troca so os dois campos de clima."""
import io, json, os, urllib.request, zipfile

MC = "1.21.1"
OUT = "src/main/resources/data/minecraft/worldgen/noise_settings/overworld.json"
PATH = "data/minecraft/worldgen/noise_settings/overworld.json"

def get(url):
    with urllib.request.urlopen(url) as r:
        return r.read()

manifest = json.loads(get("https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"))
vurl = next(v["url"] for v in manifest["versions"] if v["id"] == MC)
server_url = json.loads(get(vurl))["downloads"]["server"]["url"]
outer = zipfile.ZipFile(io.BytesIO(get(server_url)))

def find(z):
    return PATH if PATH in z.namelist() else None

data = None
if find(outer):
    data = outer.read(PATH)
else:
    for name in outer.namelist():
        if name.startswith("META-INF/versions/") and name.endswith(".jar"):
            inner = zipfile.ZipFile(io.BytesIO(outer.read(name)))
            if find(inner):
                data = inner.read(PATH)
                break
if data is None:
    raise SystemExit("overworld.json nao encontrado no server.jar")

js = json.loads(data)
js["noise_router"]["temperature"] = "latclima:temperature"
js["noise_router"]["vegetation"] = "latclima:humidity"
os.makedirs(os.path.dirname(OUT), exist_ok=True)
with open(OUT, "w") as f:
    json.dump(js, f, indent=2)
print("ok:", OUT)
