import Cookies from 'js-cookie'

export function getToken() {
  return Cookies.get("token")
}
export function setToken(token) {
 /* let inFifteenMinutes = new Date(new Date().getTime() + 60 * 60 * 1000);
  return Cookies.set("token", token,{expires:inFifteenMinutes})*/
  return Cookies.set("token", token)
}
export function removeToken() {
  return Cookies.remove("token")
}

export function hasAuth(perms, perm) {
  let hasA = false
  perms.forEach(p => {
    if (p.indexOf(perm) !== -1) {
      hasA = true
      return false
    }
  })
  return hasA
}
