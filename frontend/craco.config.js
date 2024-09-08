const path = require('path');

module.exports = {
  webpack: {
    alias: {
      '@/Navigation': path.resolve(__dirname, 'src/Navigation/index.ts'),
      '@/Redux': path.resolve(__dirname, 'src/redux/index.ts'),
      '@/Types': path.resolve(__dirname, 'src/Types/index.ts'),
      '@/Features/Customer/OrderHistory': path.resolve(__dirname, 'src/Features/Buyer/OrderHistory/index.ts'),
      '@/Features/Customer/ShoppingCart': path.resolve(__dirname, 'src/Features/Buyer/ShoppingCart/index.ts'),
      '@/Features/Customer/Store': path.resolve(__dirname, 'src/Features/Buyer/Store/index.ts'),
      '@/Features/Seller/Orders': path.resolve(__dirname, 'src/Features/Seller/Orders/index.ts'),
      '@/Features/Seller/Product': path.resolve(__dirname, 'src/Features/Seller/Product/index.ts'),
      '@/Features/NavigationBar': path.resolve(__dirname, 'src/Features/NavigationBar/index.ts'),
      '@/Features/Notifications': path.resolve(__dirname, 'src/Features/Notifications/index.ts'),
      '@/Features/Common/Componenets': path.resolve(__dirname, 'src/Features/Common/components/index.ts'),
      '@/Features/Authentication': path.resolve(__dirname, 'src/Features/Authentication/index.ts'),
    },
  },
};