const identityApiUrl = process.env.NEXT_PUBLIC_IDENTITY_API_URL;

export function identityApi(path: string) {
  return `${identityApiUrl}${path}`;
}
