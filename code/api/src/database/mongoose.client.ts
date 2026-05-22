import 'dotenv/config'
import mongoose from 'mongoose'

let isConnected = false

function buildMongoUri() {
  const user = process.env.MONGO_USER
  const password = process.env.MONGO_PASSWORD
  const host = process.env.MONGO_HOST ?? 'localhost'
  const port = process.env.MONGO_PORT ?? '27017'
  const dbName = process.env.MONGO_DB_NAME ?? 'mongo_db'

  if (!user || !password) {
    throw new Error('Missing MongoDB credentials in environment variables.')
  }

  return `mongodb://${encodeURIComponent(user)}:${encodeURIComponent(password)}@${host}:${port}/${dbName}?authSource=admin`
}

export async function connectMongo() {
  if (isConnected) return

  const uri = buildMongoUri()
  await mongoose.connect(uri)
  isConnected = true
}
