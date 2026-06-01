import * as admin from 'firebase-admin'
import type { ServiceAccount } from 'firebase-admin'
import type { Messaging } from 'firebase-admin/messaging'

function requireEnv(name: string): string {
  const value = process.env[name]
  if (!value) throw new Error(`Missing required environment variable: ${name}`)
  return value
}

export function initializeFirebaseSDK(): void {
  if (admin.apps.length) return

  const serviceAccount: ServiceAccount = {
    projectId:   requireEnv('FCM_PROJECT_ID'),
    clientEmail: requireEnv('FCM_CLIENT_EMAIL'),
    privateKey:  requireEnv('FCM_PRIVATE_KEY').replace(/\\n/g, '\n'),
  }

  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
  })

  console.info('Firebase SDK initialized')
}

export function getMessaging(): Messaging {
  return admin.messaging()
}