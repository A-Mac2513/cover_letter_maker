import type { MetadataRoute } from 'next'
 
export default function manifest(): MetadataRoute.Manifest {
  return {
    name: 'Cover Letter Maker',
    short_name: 'CoverLetterMaker',
    description: 'A web app to generate cover letters for job applications.',
    start_url: '/',
    display: 'standalone',
    background_color: '#ffffff',
    theme_color: '#000000',
    icons: [
      {
        src: './icons/yoda2.png',
        sizes: '192x192',
        type: 'image/png',
      },
      {
        src: './icons/donatello2.png',
        sizes: '512x512',
        type: 'image/png',
      },
    ],
  }
}