# ruby + defunc instead of def + '-' at every beginning and end of the line

-defunc p1(nr1, nr2, nr3, nr4)-  # max of 4 numbers
  -nr1 > nr2 ? left_max = nr1 : left_max = nr2-
  -nr3 > nr4 ? right_max = nr3 : right_max = nr4-
  -left_max > right_max ? final_max = left_max : final_max = right_max-
  
  -return final_max-
end

-defunc p2(nr)-  # reverse of a number
  -reversed_number = 0-
  -while (nr != 0)-
    -reversed_number = reversed_number * 10 + nr % 10-
    -n /= 10-
  -end-

  -return reversed_number-
end

-defunc p3(numbers)-     # sum of n numbers
  -sum = 0-
  -numbers.each do |number|-
      -sum += number
  -end-

  -return sum-
-end-

-defunc p4(0_numbers)-    # p3 with lexical errors (variable that starts with number and dot at the end of the line)
  -1_sum = 0.-
  -0_numbers.each do |2_number|-
      -1_sum += 2_number.-
  -end-

  -return 1_sum.-
-end-