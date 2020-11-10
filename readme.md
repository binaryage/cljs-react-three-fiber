This example app is a rewrite of [react-three-fiber/examples](https://github.com/react-spring/react-three-fiber)

### WORK-IN-PROGRESS

This project is a good excuse for me spend time learning some modern JS stuff and recent CLJS tools:

* [shadow-cljs](https://github.com/thheller/shadow-cljs)
* [modern react](https://reactjs.org/blog/2019/02/06/react-v16.8.0.html)
* [helix](https://github.com/Lokeh/helix)
* [three.js](https://github.com/mrdoob/three.js)
* [react-three-fiber](https://github.com/react-spring/react-three-fiber)  

#### Initial setup

```bash
git clone https://github.com/binaryage/cljs-react-three-fiber.git
cd cljs-react-three-fiber
``` 

#### Development workflow

```bash
yarn install
shadow-cljs watch app
``` 

Then:
1. wait for compilation to finish
2. visit dev server at [http://localhost:8080](http://localhost:8080)

#### Release workflow

```bash
yarn install
shadow-cljs release app
python -m http.server 8000 --directory public
``` 

Then visit server at [http://localhost:8000](http://localhost:8000)

#### Shadow CLJS report

```bash
./scripts/report.sh
```
