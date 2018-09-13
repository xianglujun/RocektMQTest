class Solution {
  public static int[] twoSum(int[] nums, int target) {
    for (int i = 0, len = nums.length; i <= len - 1; i++) {
      int first = i;
      int ele = nums[first];
      for (int next = i + 1; next < len; next++) {
        int ele1 = nums[next];
        if ((ele1 + ele) == target) {
          return new int[]{first, next};
        }
      }
    }

    return new int[0];
  }

  public static void main(String[] args) {
    System.out.println(String.valueOf(Solution.twoSum(new int[]{3,2,3}, 6)));
  }
}