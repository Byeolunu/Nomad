export interface Category {
  name: string;
  icon: string;
  count: number;
}

export interface Testimonial {
  id: number;
  name: string;
  role: string;
  comment: string;
  rating: number;
  avatar: string;
}

export const categories: Category[] = [
  { name: 'Development', icon: 'Code2', count: 245 },
  { name: 'Design', icon: 'Palette', count: 189 },
  { name: 'Writing', icon: 'PenTool', count: 156 },
  { name: 'Marketing', icon: 'Megaphone', count: 134 },
  { name: 'Video', icon: 'Video', count: 98 },
  { name: 'Translation', icon: 'Languages', count: 87 }
];

export const testimonials: Testimonial[] = [
  {
    id: 1,
    name: 'Ahmed Benali',
    role: 'Freelancer',
    comment: 'Nomad has completely transformed how I find work. The platform is intuitive and I\'ve connected with amazing clients.',
    rating: 5,
    avatar: 'AB'
  },
  {
    id: 2,
    name: 'Fatima Alami',
    role: 'Company Owner',
    comment: 'Finding talented freelancers was always a challenge. Nomad made it so easy to post missions and hire the perfect team.',
    rating: 5,
    avatar: 'FA'
  },
  {
    id: 3,
    name: 'Youssef Idrissi',
    role: 'Freelancer',
    comment: 'The best freelance platform in Morocco. Great community, fair pricing, and excellent support.',
    rating: 5,
    avatar: 'YI'
  }
];