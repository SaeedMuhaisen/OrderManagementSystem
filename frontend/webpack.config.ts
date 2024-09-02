const path = require('path');

const config = {
    resolve: {
        alias: {
            "@/Navigation": path.resolve(__dirname, "src/Navigation/index.ts"),
        }
    }
}