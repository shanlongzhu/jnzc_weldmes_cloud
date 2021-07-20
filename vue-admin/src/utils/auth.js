import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}



export function getPublicToken() {
  return Cookies.get('publicToken')
}

export function setPublicToken(token) {
  return Cookies.set('publicToken', token)
}

export function removePublicToken() {
  return Cookies.remove('publicToken')
}