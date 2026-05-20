import { prisma } from '../../lib/prisma.js';

async function seed() {
    await prisma.follow.deleteMany();
    
    // Fetch existing users to ensure we use valid IDs
    const users = await prisma.user.findMany({
        take: 20,
        select: { id: true }
    });

    if (users.length < 2) {
        console.log('Not enough users to create follows.');
        return;
    }

    const follows: { followerId: number; followedId: number }[] = [];
    const mainUser = users[0]?.id as number;
    const secondUser = users[1]?.id as number;

    for (let i = 0; i < users.length; i++) {
        const userId = users[i]?.id as number;
        if (userId !== secondUser) {
            follows.push({ followerId: secondUser, followedId: userId });
        }
        if (userId !== mainUser) {
            follows.push({ followerId: userId, followedId: mainUser });
        }
    }

    const unique = follows.filter(
        (f, index, self) =>
            index === self.findIndex(t => t.followerId === f.followerId && t.followedId === f.followedId)
    );

    await prisma.follow.createMany({ data: unique });
}

seed()
    .then(() => console.log('Seeding completed successfully.'))
    .catch(console.error)
    .finally(() => prisma.$disconnect());