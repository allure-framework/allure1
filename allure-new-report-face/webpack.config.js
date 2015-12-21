/*eslint-env node*/
const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

function makeConfig(hotload) {
    return {
        entry: ['./src/index.js'],
        output: {
            path: path.join(__dirname, 'target/www'),
            pathinfo: true,
            filename: 'app.js'
        },
        module: {
            loaders: [{
                test: /\.js$/,
                exclude: /node_modules/,
                loader: 'babel-loader'
            }, {
                test: /\.(png|svg|woff2?|ttf|eot)(\?.*)?$/,
                loader: 'url-loader',
                query: {
                    limit: 10000
                }
            }, {
                test:   /\.css$/,
                loader: hotload ? 'style-loader!css-loader!postcss-loader' : ExtractTextPlugin.extract('style-loader', 'css-loader!postcss-loader')
            }, {
                test: /\.hbs$/,
                loader: 'handlebars-loader',
                query: {
                    helperDirs: [
                        path.join(__dirname, 'src/helpers'),
                        path.join(__dirname, 'src/blocks')
                    ]
                }
            }]
        },
        devtool: 'source-map',
        plugins: (() => {
            const plugins = [
                new HtmlWebpackPlugin({
                    template: './src/index.html',
                    inject: 'body',
                    favicon: './src/favicon.ico'
                }),
                new ExtractTextPlugin('styles.css')
            ];
            return hotload ? plugins.concat(new webpack.HotModuleReplacementPlugin()) : plugins;
        })(),
        postcss: (webpack) => [
            require('postcss-import')({addDependencyTo: webpack}),
            require('precss')({'import': {disable: true}}),
            require('autoprefixer')
        ]
    };
}

module.exports = makeConfig();
module.exports.factory = makeConfig;
