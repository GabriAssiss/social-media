import { getMessaging } from '../database/firebase.client.js'

export async function sendMessageNotification({
    fcmToken,
    senderName,
    text,
    receiverId,
}: {
    fcmToken: string
    senderName: string
    text: string
    receiverId: number
}): Promise<void> {
    await getMessaging().send({
        token: fcmToken,
        notification: {
            title: senderName,
            body: text.slice(0, 100),
        },
        data: {
            type: 'new_message',
            receiverId: String(receiverId),
        },
        android: { priority: 'high' },
    })
}