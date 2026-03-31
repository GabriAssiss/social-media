import { prisma } from '../../lib/prisma.js';
import bcrypt from 'bcrypt';

const users = [
    { name: "Alice Souza", email: "alice@exemplo.com", password: "senha_123", phone: "11999990001" },
    { name: "Bruno Lima", email: "bruno@exemplo.com", password: "senha_123", phone: "11999990002" },
    { name: "Carla Mendes", email: "carla@exemplo.com", password: "senha_123", phone: "11999990003" },
    { name: "Diego Rocha", email: "diego@exemplo.com", password: "senha_123", phone: "11999990004" },
    { name: "Elena Costa", email: "elena@exemplo.com", password: "senha_123", phone: "11999990005" },
    { name: "Felipe Dias", email: "felipe@exemplo.com", password: "senha_123", phone: "11999990006" },
    { name: "Gabriela Nunes", email: "gabriela@exemplo.com", password: "senha_123", phone: "11999990007" },
    { name: "Henrique Alves", email: "henrique@exemplo.com", password: "senha_123", phone: "11999990008" },
    { name: "Isabela Ferreira", email: "isabela@exemplo.com", password: "senha_123", phone: "11999990009" },
    { name: "João Martins", email: "joao@exemplo.com", password: "senha_123", phone: "11999990010" },
    { name: "Karen Oliveira", email: "karen@exemplo.com", password: "senha_123", phone: "11999990011" },
    { name: "Lucas Pereira", email: "lucas@exemplo.com", password: "senha_123", phone: "11999990012" },
    { name: "Mariana Gomes", email: "mariana@exemplo.com", password: "senha_123", phone: "11999990013" },
    { name: "Nicolas Barbosa", email: "nicolas@exemplo.com", password: "senha_123", phone: "11999990014" },
    { name: "Olivia Cardoso", email: "olivia@exemplo.com", password: "senha_123", phone: "11999990015" },
    { name: "Pedro Ribeiro", email: "pedro@exemplo.com", password: "senha_123", phone: "11999990016" },
    { name: "Quintina Melo", email: "quintina@exemplo.com", password: "senha_123", phone: "11999990017" },
    { name: "Rafael Teixeira", email: "rafael@exemplo.com", password: "senha_123", phone: "11999990018" },
    { name: "Sara Vieira", email: "sara@exemplo.com", password: "senha_123", phone: "11999990019" },
    { name: "Thiago Correia", email: "thiago@exemplo.com", password: "senha_123", phone: "11999990020" },
];

async function seed() {
    const data = await Promise.all(
        users.map(async (user) => ({
            ...user,
            password: await bcrypt.hash(user.password, 10)
        }))
    );

    await prisma.user.deleteMany();
    await prisma.user.createMany({ data });
}

seed()
    .catch(console.error)
    .finally(() => prisma.$disconnect());