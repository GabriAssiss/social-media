import mongoose, { Schema } from "mongoose";

const messageSchema = new Schema({
    senderId : { type: Number, required: true },
    receiverId : { type: Number, required: true },
    text: { type: String, required: false },
    image : { type: String, required: false },
    createdAt : { type: Date, default: Date.now },
    read: { type: Boolean, default: false }
})

messageSchema.index({ senderId: 1, receiverId: 1, createdAt: -1 });
messageSchema.index({ receiverId: 1, senderId: 1, createdAt: -1 });

export const Message = mongoose.model("Message", messageSchema)
