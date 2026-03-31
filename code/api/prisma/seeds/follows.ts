import { prisma } from '../../lib/prisma.js';

async function seed() {
    await prisma.follow.deleteMany();
    const follows: { followerId: number; followedId: number }[] = [];

    for (let i = 1; i <= 20; i++) {
        if (i !== 2) {
            follows.push({ followerId: 2, followedId: i });
        }
        if (i !== 1) {
            follows.push({ followerId: i, followedId: 1 });
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