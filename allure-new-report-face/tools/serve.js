/*eslint-env node*/
const webpack = require('webpack');
const config = require('../webpack.config').factory(true);
const express = require('express');
const WebpackDevServer = require('webpack-dev-server');
const port = process.env.PORT || 3000;

config.entry.unshift(`webpack-dev-server/client?http://localhost:${port}`, 'webpack/hot/dev-server');
const compiler = webpack(config);
const server = new WebpackDevServer(compiler, {
    contentBase: './target/www',
    stats: { colors: true },
    inline: true,
    hot: true
});
server.use('/data', express.static('./target/allure-report/data'));
server.listen(port);


/*
 const webpack = require('webpack');
 const config = require('../webpack.config').factory(true);
 const webpackMiddleware = require('webpack-dev-middleware');
 const port = process.env.PORT || 3000;
 const express = require('express');
 const app = express();

 config.entry.unshift(`webpack-dev-server/client?http://localhost:${port}`, 'webpack/hot/dev-server');
 const compiler = webpack(config);
 app.use('/data', express.static('../target/allure-report/data'));
 app.use(webpackMiddleware(compiler, {
 contentBase: './target/www',
 stats: { colors: true },
 inline: true,
 hot: true
 }));

 app.listen(port);

 */
