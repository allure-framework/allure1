var express = require('express');
var app = express();
app.use(express.static('../target/webjars/META-INF/resources'));
app.use(express.static('../target/allure-report'));
app.use(express.static('../src/main/webapp'));

app.listen(process.env.PORT || 8080);
