import mongoose, { Schema } from "mongoose";

const messageSchema = new Schema({
    senderId : { type: Number, required: true },
    receiverId : { type: Number, required: true },
    text: { type: String, required: false },
    image : { type: String, required: false },
    createdAt : { type: Date, default: Date.now },
    read: { type: Boolean, default: false }
})

export const Message = mongoose.model("Message", messageSchema)
