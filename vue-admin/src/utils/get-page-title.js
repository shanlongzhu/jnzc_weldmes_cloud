import defaultSettings from '@/settings'

const title = defaultSettings.title || 'WELDMES'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
