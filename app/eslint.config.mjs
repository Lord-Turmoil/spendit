import eslint from "@eslint/js";
import tseslint from "typescript-eslint";
import vueParser from "vue-eslint-parser"
import tsParser from "@typescript-eslint/parser"

export default tseslint.config(
  eslint.configs.recommended,
  tseslint.configs.recommended,
  {
    files: ["**/*.ts", "**/*.vue"],
    languageOptions: {
      parser: vueParser,
      parserOptions: {
        parser: tsParser
      }
    },
    rules: {
      semi: "error",
      quotes: ["error", "single", { "avoidEscape": true }],
      "@typescript-eslint/no-explicit-any": "off",
      "@typescript-eslint/no-unused-vars": "off",
      "@typescript-eslint/no-empty-object-type": "off"
    }
  },
);
