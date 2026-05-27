import app from './app.js';
import { connectMongo } from './database/mongoose.client.js';
import { createServer } from 'node:http';
import { initSocket } from './socket/socket.js';

const PORT = process.env.PORT || 3000;

async function bootstrap() {
  await connectMongo();

  const server = createServer(app);
  initSocket(server);

  server.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
  });
}

bootstrap().catch((error) => {
  console.error('Failed to start server:', error);
  process.exit(1);
});