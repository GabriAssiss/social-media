import { cert, getApps, initializeApp } from 'firebase-admin/app'
import { getMessaging } from 'firebase-admin/messaging'
import type { Messaging } from 'firebase-admin/messaging'

function requireEnv(name: string): string {
  const value = process.env[name]
  if (!value) throw new Error(`Missing required environment variable: ${name}`)
  return value
}

export function initializeFirebaseSDK(): void {
  if (getApps().length) return

  const serviceAccount = {
    projectId:   requireEnv('FCM_PROJECT_ID'),
    clientEmail: requireEnv('FCM_CLIENT_EMAIL'),
    privateKey:  requireEnv('FCM_PRIVATE_KEY').replace(/\\n/g, '\n'),
  }

  initializeApp({
    credential: cert(serviceAccount),
  })

  console.info('Firebase SDK initialized')
}

export { getMessaging }
export type { Messaging }