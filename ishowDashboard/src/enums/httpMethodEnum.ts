export const HttpMethod = {
  GET: 'GET',
  POST: 'POST',
  DELETE: 'DELETE',
  PUT: 'PUT',
  PATCH: 'PATCH',
} as const

export type HttpMethod = typeof HttpMethod[keyof typeof HttpMethod]
