import type { ImageRatioType } from "../../enums/ImageRatioEnum";
import { validateImageRatio } from "../../utils/imageValidation";

export const bannerRules = {
  //   required: (field: string) => (v: any) =>
  //     !!v || `${field} is required`,

  //   dateRange:
  //     (startDate: () => string) =>
  //     (endDate: string) => {
  //       if (!startDate() || !endDate) return true;
  //       return (
  //         new Date(startDate()) <= new Date(endDate) ||
  //         "End Date must be after Start Date"
  //       );
  //     },

  //   imageRequired: (files: File[]) =>
  //     files?.length > 0 || "Image is required",

  //   imageType: (files: File[]) => {
  //     if (!files?.length) return true;
  //     return files[0].type.startsWith("image/")
  //       || "File must be an image";
  //   },

  imageRatio: (ratio: ImageRatioType) => async (files: File[]) => {
    if (!files?.length) return true;

    const valid = await validateImageRatio(files[0], ratio.width, ratio.height);

    return valid || `Image ratio must be ${ratio.label}`;
  },
};
