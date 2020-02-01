This example app is a rewrite of [react-three-fiber/examples](https://github.com/react-spring/react-three-fiber)

### WORK-IN-PROGRESS

#### Development workflow

```bash
npm install
shadow-cljs watch app
``` 

Then:
1. wait for compilation to finish
2. visit dev server at [http://localhost:8080](http://localhost:8080)

#### Release workflow

```bash
npm install
shadow-cljs release app
python -m http.server 8000 --directory public
``` 

Then visit server at [http://localhost:8000](http://localhost:8000)

#### Shadow CLJS report

```bash
./scripts/report.sh
```
