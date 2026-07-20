type ValidationRule = {
  (value: string): boolean | string;
  (value: number): boolean | string;
  (value: any[]): boolean | string;
};

export const rules: {
  required: ValidationRule;
  minLength: (min: number) => ValidationRule;
  maxLength: (max: number) => ValidationRule;
  notEmptyArray: ValidationRule;
  numeric: ValidationRule;
  durationFormat: ValidationRule;
} = {
  required: ((value: string | number | any[]) => {
    if (Array.isArray(value)) return value.length > 0 || "This field is required";
    return !!value || "This field is required";
  }) as ValidationRule,

  minLength: (min: number) => ((value: string | number | any[]) => {
    if (typeof value === 'number') return true;
    if (Array.isArray(value)) return value.length >= min || `Minimum ${min} items required`;
    return value.length >= min || `Minimum ${min} characters required`;
  }) as ValidationRule,

  maxLength: (max: number) => ((value: string | number | any[]) => {
    if (typeof value === 'number') return true;
    if (Array.isArray(value)) return value.length <= max || `Maximum ${max} items allowed`;
    return value.length <= max || `Maximum ${max} characters allowed`;
  }) as ValidationRule,

  notEmptyArray: ((value: any[]) =>
      (Array.isArray(value) && value.length > 0) || "This field cannot be empty"
  ) as ValidationRule,

  numeric: ((value: string | number) =>
      typeof value === "number" || "This field must be a valid number"
  ) as ValidationRule,

  durationFormat: ((value: string) => {
    if (!value) return true;
    const pattern = /^([0-1][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/;
    return pattern.test(value) || "Duration must be in HH:MM:SS format (e.g., 00:03:45)";
  }) as ValidationRule,
};
