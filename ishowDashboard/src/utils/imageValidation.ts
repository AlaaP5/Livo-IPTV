export function validateImageRatio(
  file: File,
  ratioWidth: number,
  ratioHeight: number
): Promise<boolean> {
  return new Promise((resolve) => {
    const img = new Image();
    img.onload = () => {
      resolve(img.width * ratioHeight === img.height * ratioWidth);
    };
    img.onerror = () => resolve(false);
    img.src = URL.createObjectURL(file);
  });
}
