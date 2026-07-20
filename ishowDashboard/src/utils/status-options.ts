export interface SelectOption {
  value: string;
  title: string;
}

export const createBooleanOptions = (
  trueLabel: string,
  falseLabel: string
): SelectOption[] => [
  { value: "1", title: trueLabel },
  { value: "0", title: falseLabel },
];