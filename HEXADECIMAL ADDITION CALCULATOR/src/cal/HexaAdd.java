
package cal;
import java.text.DecimalFormat;
public class HexaAdd extends javax.swing.JFrame {

  double number, ans;
  int operators;
  private boolean calculator = true;
  private boolean operatorTyped = false;
  private boolean decimalAdded = false; 
  private boolean operate = false;
  private int set = 0;
  private int call = 0;
    public HexaAdd() {
        initComponents();

        A.setEnabled(false);
        B.setEnabled(false);
        C.setEnabled(false);
        D.setEnabled(false);
        E.setEnabled(false);
        F.setEnabled(false);
        c.setSelected(true);
    }

    public void calculate(){
       String expression = InputOutput.getText();
    try {
        double result = evaluateExpression(expression);
        String formattedResult;

        if (result == (int) result) {
            formattedResult = String.format("%d", (int) result);
            decimalAdded = false;
        } else {
            formattedResult = String.format("%.2f", result);
            decimalAdded = true;
            
        }

        InputOutput.setText(formattedResult);
    } catch (Exception ex) {
       InputOutput.setText("");
    }
    operate = false;
 }
  private boolean isOperator(String input) {
    return "+-*/".contains(input);
 }
  
 
public void hexAddition() {
     
    String input = InputOutput.getText();
    String[] parts = input.split("\\+");
    
    if (parts.length == 2) {
        String hex1 = parts[0].trim();
        String hex2 = parts[1].trim();
        
        if (isValidHexadecimalWithDecimal(hex1) && isValidHexadecimalWithDecimal(hex2)) {
            String resultHex = performHexadecimalAddition(hex1, hex2);
            InputOutput.setText( resultHex);
            
        } else {
            InputOutput.setText("Invalid hexadecimal input with decimal point.");
        }
    } else {
        InputOutput.setText("Invalid input format. Please use 'hex1 + hex2' format.");
    }
}


// Check if a string is a valid hexadecimal number with a decimal point
private boolean isValidHexadecimalWithDecimal(String input) {
    return input.matches("^[0-9A-Fa-f]+(\\.[0-9A-Fa-f]+)?$");
}

// Perform hexadecimal addition
private String performHexadecimalAddition(String hex1, String hex2) {
    double decimal1 = hexToDecimalWithDecimal(hex1);
    double decimal2 = hexToDecimalWithDecimal(hex2);
    double sum = decimal1 + decimal2;
    
    // Convert the sum back to hexadecimal with uppercase letters and no trailing zeros
    String resultHex = decimalToHexWithDecimal(sum);
    
    return resultHex;
}

// Convert a hexadecimal number with a decimal point to its decimal representation
private double hexToDecimalWithDecimal(String hex) {
    String[] parts = hex.split("\\.");
    String integerPart = parts[0];
    String fractionalPart = (parts.length > 1) ? parts[1] : "";

    double decimalValue = 0.0;

    for (int i = 0; i < integerPart.length(); i++) {
        char c = Character.toUpperCase(integerPart.charAt(i));
        int digit = Character.isDigit(c) ? c - '0' : c - 'A' + 10;
        decimalValue = decimalValue * 16 + digit;
    }

    if (!fractionalPart.isEmpty()) {
        double fractionalValue = 0.0;
        double divisor = 16.0;

        for (int i = 0; i < fractionalPart.length(); i++) {
            char c = Character.toUpperCase(fractionalPart.charAt(i));
            int digit = Character.isDigit(c) ? c - '0' : c - 'A' + 10;
            fractionalValue += digit / divisor;
            divisor *= 16;
        }

        decimalValue += fractionalValue;
    }

    return decimalValue;
}


// Convert a decimal number with a decimal point to its hexadecimal representation
private String decimalToHexWithDecimal(double decimal) {
    String hexIntegerPart = Integer.toHexString((int) decimal).toUpperCase();
    double fractionalPart = decimal - (int) decimal;
    StringBuilder hexFractionalPart = new StringBuilder();

    for (int i = 0; i < 8; i++) { // Convert up to 8 decimal places
        fractionalPart *= 16;
        int digit = (int) fractionalPart;
        if (digit < 10) {
            hexFractionalPart.append(digit);
        } else {
            char hexDigit = (char) ('A' + (digit - 10));
            hexFractionalPart.append(hexDigit);
        }
        fractionalPart -= digit;
    }

    // Remove trailing zeros
    int length = hexFractionalPart.length();
    while (length > 0 && hexFractionalPart.charAt(length - 1) == '0') {
        hexFractionalPart.deleteCharAt(length - 1);
        length--;
    }

    return hexFractionalPart.length() == 0 ? hexIntegerPart : hexIntegerPart + "." + hexFractionalPart.toString();
}

  private static double evaluateExpression(String expression) {
        try {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (Character.isWhitespace(ch)) nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (; ; ) {
                        if (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (; ; ) {
                        if (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('÷')) x /= parseFactor(); // division
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else if (Character.isDigit(ch) || ch == '.') { // numbers
                        while (Character.isDigit(ch) || ch == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }

                    return x;
                }
            }.parse();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }
    }

private boolean isValidHexadecimal(String hex) {
    // Check if the string consists of valid hexadecimal characters
    return hex.matches("^[0-9A-Fa-f]+$");
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        InputOutput = new javax.swing.JTextField();
        F = new javax.swing.JButton();
        A = new javax.swing.JButton();
        C = new javax.swing.JButton();
        D = new javax.swing.JButton();
        E = new javax.swing.JButton();
        Seven = new javax.swing.JButton();
        Four = new javax.swing.JButton();
        Dot = new javax.swing.JButton();
        Eight = new javax.swing.JButton();
        Five = new javax.swing.JButton();
        Two = new javax.swing.JButton();
        Nine = new javax.swing.JButton();
        Six = new javax.swing.JButton();
        Three = new javax.swing.JButton();
        One = new javax.swing.JButton();
        Zero = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        Equals = new javax.swing.JButton();
        Divide = new javax.swing.JButton();
        Minus = new javax.swing.JButton();
        Plus = new javax.swing.JButton();
        Sign = new javax.swing.JButton();
        ClearAll = new javax.swing.JButton();
        B = new javax.swing.JButton();
        h = new javax.swing.JRadioButton();
        c = new javax.swing.JRadioButton();
        Equation = new javax.swing.JLabel();
        Times = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setForeground(new java.awt.Color(153, 153, 153));
        jPanel1.setPreferredSize(new java.awt.Dimension(404, 735));
        jPanel1.setLayout(null);

        InputOutput.setBackground(new java.awt.Color(0, 51, 51));
        InputOutput.setFont(new java.awt.Font("Dubai Medium", 1, 48)); // NOI18N
        InputOutput.setForeground(new java.awt.Color(255, 255, 255));
        InputOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputOutputActionPerformed(evt);
            }
        });
        InputOutput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                InputOutputKeyPressed(evt);
            }
        });
        jPanel1.add(InputOutput);
        InputOutput.setBounds(10, 10, 440, 170);

        F.setBackground(new java.awt.Color(0, 51, 51));
        F.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        F.setForeground(new java.awt.Color(255, 255, 255));
        F.setText("F");
        F.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        F.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FActionPerformed(evt);
            }
        });
        jPanel1.add(F);
        F.setBounds(10, 510, 80, 50);

        A.setBackground(new java.awt.Color(0, 51, 51));
        A.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        A.setForeground(new java.awt.Color(255, 255, 255));
        A.setText("A");
        A.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        A.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AActionPerformed(evt);
            }
        });
        jPanel1.add(A);
        A.setBounds(100, 270, 80, 50);

        C.setBackground(new java.awt.Color(0, 51, 51));
        C.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        C.setForeground(new java.awt.Color(255, 255, 255));
        C.setText("C");
        C.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        C.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CActionPerformed(evt);
            }
        });
        jPanel1.add(C);
        C.setBounds(10, 330, 80, 50);

        D.setBackground(new java.awt.Color(0, 51, 51));
        D.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        D.setForeground(new java.awt.Color(255, 255, 255));
        D.setText("D");
        D.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DActionPerformed(evt);
            }
        });
        jPanel1.add(D);
        D.setBounds(10, 390, 80, 50);

        E.setBackground(new java.awt.Color(0, 51, 51));
        E.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        E.setForeground(new java.awt.Color(255, 255, 255));
        E.setText("E");
        E.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        E.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EActionPerformed(evt);
            }
        });
        jPanel1.add(E);
        E.setBounds(10, 450, 80, 50);

        Seven.setBackground(new java.awt.Color(0, 51, 51));
        Seven.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Seven.setForeground(new java.awt.Color(255, 255, 255));
        Seven.setText("7");
        Seven.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Seven.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SevenActionPerformed(evt);
            }
        });
        jPanel1.add(Seven);
        Seven.setBounds(100, 330, 80, 50);

        Four.setBackground(new java.awt.Color(0, 51, 51));
        Four.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Four.setForeground(new java.awt.Color(255, 255, 255));
        Four.setText("4");
        Four.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Four.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FourActionPerformed(evt);
            }
        });
        jPanel1.add(Four);
        Four.setBounds(100, 390, 80, 50);

        Dot.setBackground(new java.awt.Color(0, 51, 51));
        Dot.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Dot.setForeground(new java.awt.Color(255, 255, 255));
        Dot.setText(".");
        Dot.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Dot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DotActionPerformed(evt);
            }
        });
        jPanel1.add(Dot);
        Dot.setBounds(190, 510, 80, 50);

        Eight.setBackground(new java.awt.Color(0, 51, 51));
        Eight.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Eight.setForeground(new java.awt.Color(255, 255, 255));
        Eight.setText("8");
        Eight.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Eight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EightActionPerformed(evt);
            }
        });
        jPanel1.add(Eight);
        Eight.setBounds(190, 330, 80, 50);

        Five.setBackground(new java.awt.Color(0, 51, 51));
        Five.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Five.setForeground(new java.awt.Color(255, 255, 255));
        Five.setText("5");
        Five.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Five.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiveActionPerformed(evt);
            }
        });
        jPanel1.add(Five);
        Five.setBounds(190, 390, 80, 50);

        Two.setBackground(new java.awt.Color(0, 51, 51));
        Two.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Two.setForeground(new java.awt.Color(255, 255, 255));
        Two.setText("2");
        Two.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Two.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TwoActionPerformed(evt);
            }
        });
        jPanel1.add(Two);
        Two.setBounds(190, 450, 80, 50);

        Nine.setBackground(new java.awt.Color(0, 51, 51));
        Nine.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Nine.setForeground(new java.awt.Color(255, 255, 255));
        Nine.setText("9");
        Nine.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Nine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NineActionPerformed(evt);
            }
        });
        jPanel1.add(Nine);
        Nine.setBounds(280, 330, 80, 50);

        Six.setBackground(new java.awt.Color(0, 51, 51));
        Six.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Six.setForeground(new java.awt.Color(255, 255, 255));
        Six.setText("6");
        Six.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Six.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SixActionPerformed(evt);
            }
        });
        jPanel1.add(Six);
        Six.setBounds(280, 390, 80, 50);

        Three.setBackground(new java.awt.Color(0, 51, 51));
        Three.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Three.setForeground(new java.awt.Color(255, 255, 255));
        Three.setText("3");
        Three.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Three.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThreeActionPerformed(evt);
            }
        });
        jPanel1.add(Three);
        Three.setBounds(280, 450, 80, 50);

        One.setBackground(new java.awt.Color(0, 51, 51));
        One.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        One.setForeground(new java.awt.Color(255, 255, 255));
        One.setText("1");
        One.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        One.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OneActionPerformed(evt);
            }
        });
        jPanel1.add(One);
        One.setBounds(100, 450, 80, 50);

        Zero.setBackground(new java.awt.Color(0, 51, 51));
        Zero.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Zero.setForeground(new java.awt.Color(255, 255, 255));
        Zero.setText("0");
        Zero.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Zero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZeroActionPerformed(evt);
            }
        });
        jPanel1.add(Zero);
        Zero.setBounds(100, 510, 80, 50);

        Delete.setBackground(new java.awt.Color(0, 51, 51));
        Delete.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Delete.setForeground(new java.awt.Color(255, 255, 255));
        Delete.setText("DEL");
        Delete.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        jPanel1.add(Delete);
        Delete.setBounds(190, 270, 80, 50);

        Equals.setBackground(new java.awt.Color(0, 51, 51));
        Equals.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Equals.setForeground(new java.awt.Color(255, 255, 255));
        Equals.setText("=");
        Equals.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Equals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EqualsActionPerformed(evt);
            }
        });
        jPanel1.add(Equals);
        Equals.setBounds(280, 510, 80, 50);

        Divide.setBackground(new java.awt.Color(0, 51, 51));
        Divide.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Divide.setForeground(new java.awt.Color(255, 255, 255));
        Divide.setText("÷");
        Divide.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Divide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DivideActionPerformed(evt);
            }
        });
        jPanel1.add(Divide);
        Divide.setBounds(370, 390, 80, 50);

        Minus.setBackground(new java.awt.Color(0, 51, 51));
        Minus.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Minus.setForeground(new java.awt.Color(255, 255, 255));
        Minus.setText("-");
        Minus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinusActionPerformed(evt);
            }
        });
        jPanel1.add(Minus);
        Minus.setBounds(370, 450, 80, 50);

        Plus.setBackground(new java.awt.Color(0, 51, 51));
        Plus.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Plus.setForeground(new java.awt.Color(255, 255, 255));
        Plus.setText("+");
        Plus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlusActionPerformed(evt);
            }
        });
        Plus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PlusKeyPressed(evt);
            }
        });
        jPanel1.add(Plus);
        Plus.setBounds(370, 510, 80, 50);

        Sign.setBackground(new java.awt.Color(0, 51, 51));
        Sign.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Sign.setForeground(new java.awt.Color(255, 255, 255));
        Sign.setText("+/-");
        Sign.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Sign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignActionPerformed(evt);
            }
        });
        jPanel1.add(Sign);
        Sign.setBounds(370, 270, 80, 50);

        ClearAll.setBackground(new java.awt.Color(0, 51, 51));
        ClearAll.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        ClearAll.setForeground(new java.awt.Color(255, 255, 255));
        ClearAll.setText("CE");
        ClearAll.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearAllActionPerformed(evt);
            }
        });
        jPanel1.add(ClearAll);
        ClearAll.setBounds(280, 270, 80, 50);

        B.setBackground(new java.awt.Color(0, 51, 51));
        B.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        B.setForeground(new java.awt.Color(255, 255, 255));
        B.setText("B");
        B.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BActionPerformed(evt);
            }
        });
        jPanel1.add(B);
        B.setBounds(10, 270, 80, 50);

        h.setBackground(new java.awt.Color(0, 51, 51));
        h.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        h.setForeground(new java.awt.Color(255, 255, 255));
        h.setText("HEX");
        h.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        h.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hActionPerformed(evt);
            }
        });
        jPanel1.add(h);
        h.setBounds(370, 220, 80, 40);

        c.setBackground(new java.awt.Color(0, 51, 51));
        c.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        c.setForeground(new java.awt.Color(255, 255, 255));
        c.setText("CAL");
        c.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cActionPerformed(evt);
            }
        });
        jPanel1.add(c);
        c.setBounds(370, 190, 80, 40);
        jPanel1.add(Equation);
        Equation.setBounds(350, 30, 0, 0);

        Times.setBackground(new java.awt.Color(0, 51, 51));
        Times.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        Times.setForeground(new java.awt.Color(255, 255, 255));
        Times.setText("x");
        Times.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Times.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimesActionPerformed(evt);
            }
        });
        jPanel1.add(Times);
        Times.setBounds(370, 330, 80, 50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FActionPerformed
        InputOutput.setText(InputOutput.getText()+"F");
         Equation.setText(Equation.getText()+"F");
    }//GEN-LAST:event_FActionPerformed

    private void AActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AActionPerformed
      InputOutput.setText(InputOutput.getText()+"A");
       Equation.setText(Equation.getText()+"A");
      
    }//GEN-LAST:event_AActionPerformed

    private void CActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CActionPerformed
        InputOutput.setText(InputOutput.getText()+"C");
         Equation.setText(Equation.getText()+"C");
    }//GEN-LAST:event_CActionPerformed

    private void DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DActionPerformed
        InputOutput.setText(InputOutput.getText()+"D");
         Equation.setText(Equation.getText()+"D");
    }//GEN-LAST:event_DActionPerformed

    private void EActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EActionPerformed
        InputOutput.setText(InputOutput.getText()+"E");
         Equation.setText(Equation.getText()+"E");
    }//GEN-LAST:event_EActionPerformed

    private void SevenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SevenActionPerformed
        InputOutput.setText(InputOutput.getText()+"7");
         Equation.setText(Equation.getText()+"7");
          operate = false;
    }//GEN-LAST:event_SevenActionPerformed

    private void FourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FourActionPerformed
        InputOutput.setText(InputOutput.getText()+"4");
         Equation.setText(Equation.getText()+"4");
          operate = false;
         
    }//GEN-LAST:event_FourActionPerformed

    private void DotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DotActionPerformed
           String currentText = InputOutput.getText();

    // Check if the last character is an operator other than '+'
    char lastChar = currentText.length() > 0 ? currentText.charAt(currentText.length() - 1) : ' ';
    if (isOperator(String.valueOf(lastChar)) && lastChar != '+') {
        // If the last character is an operator other than '+', append "0." before adding the dot
        InputOutput.setText(currentText + "0.");
    } else {
        // If the last character is not an operator, check if the current number already has a dot
        String[] parts = currentText.split("[+\\-*/]");
        String lastNumber = parts[parts.length - 1];

        if (!lastNumber.contains(".")) {
            // If the current number does not have a dot, add one
            InputOutput.setText(currentText + ".");
        }
    }

    decimalAdded = true;
    }//GEN-LAST:event_DotActionPerformed

    private void EightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EightActionPerformed
        InputOutput.setText(InputOutput.getText()+"8");
         Equation.setText(Equation.getText()+"8");
          operate = false;
    }//GEN-LAST:event_EightActionPerformed

    private void FiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiveActionPerformed
        InputOutput.setText(InputOutput.getText()+"5");
         Equation.setText(Equation.getText()+"5");
          operate = false;
    }//GEN-LAST:event_FiveActionPerformed

    private void TwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TwoActionPerformed
        InputOutput.setText(InputOutput.getText()+"2");
         Equation.setText(Equation.getText()+"2");
          operate = false;
    }//GEN-LAST:event_TwoActionPerformed

    private void NineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NineActionPerformed
        InputOutput.setText(InputOutput.getText()+"9");
         Equation.setText(Equation.getText()+"9");
          operate = false;
    }//GEN-LAST:event_NineActionPerformed

    private void SixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SixActionPerformed
        InputOutput.setText(InputOutput.getText()+"6");
         Equation.setText(Equation.getText()+"6");
          operate = false;
    }//GEN-LAST:event_SixActionPerformed

    private void ThreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThreeActionPerformed
        InputOutput.setText(InputOutput.getText()+"3");
         Equation.setText(Equation.getText()+"3");
          operate = false;
    }//GEN-LAST:event_ThreeActionPerformed

    private void OneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OneActionPerformed
        InputOutput.setText(InputOutput.getText()+"1");
         Equation.setText(Equation.getText()+"1");
          operate = false;
    }//GEN-LAST:event_OneActionPerformed

    private void ZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZeroActionPerformed
        InputOutput.setText(InputOutput.getText()+"0");
         Equation.setText(Equation.getText()+"0");
          operate = false;
    }//GEN-LAST:event_ZeroActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
       int length = InputOutput.getText().length();
       int num = InputOutput.getText().length()-1;
       
       String store;
       
       if (length > 0){
       StringBuilder back = new StringBuilder (InputOutput.getText());
       back.deleteCharAt(num);
       store = back.toString();
       InputOutput.setText(store);
       }
         decimalAdded = false;
         operate = false;
         
    }//GEN-LAST:event_DeleteActionPerformed

    private void EqualsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EqualsActionPerformed
        if(c.isSelected()){
           calculate();
        }else if (h.isSelected()){
           hexAddition();
           Plus.setEnabled(true);
        }else{
            
        }    
      set=0;
    }//GEN-LAST:event_EqualsActionPerformed

    private void DivideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DivideActionPerformed
           if (!operate) {
        if(InputOutput.getText().equals("")){
           operate = false; 
        }else{
        InputOutput.setText(InputOutput.getText() + "÷");
        operate = true;
        }
    }
        decimalAdded = false;
         
    }//GEN-LAST:event_DivideActionPerformed

    private void MinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinusActionPerformed
        if (!operate) {
            InputOutput.setText(InputOutput.getText() + "-");
            operate = true;
        }
       decimalAdded = false;  
    }//GEN-LAST:event_MinusActionPerformed

    private void PlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlusActionPerformed
        if(set==0){
        if (!operate) {
            InputOutput.setText(InputOutput.getText() + "+");
            operate = true;
        }
            decimalAdded = true; 
        }
      if(call!=0){
        set=1;
      }
      if(set==1){
            Plus.setEnabled(false);
      }
    }//GEN-LAST:event_PlusActionPerformed

    private void SignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignActionPerformed
         try {
    String input = InputOutput.getText();
        int lastIndex = -1;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '÷') {
                lastIndex = i;
            }
        }
        if (lastIndex != -1) {
            String beforeOperator = input.substring(0, lastIndex + 1);
            String afterOperator = input.substring(lastIndex + 1);
            if (afterOperator.contains(".")) {
                double number = Double.parseDouble(afterOperator);
                number = -number;
                afterOperator = String.valueOf(number);
            } else {
                int number = Integer.parseInt(afterOperator);
                number = -number;
                afterOperator = String.valueOf(number);
            }
            String updatedInput = beforeOperator + afterOperator;
            InputOutput.setText(updatedInput.replaceAll("--", ""));
        } else { 
            if (input.startsWith("-")) { 
                InputOutput.setText(input.substring(1));
            } else { 
                InputOutput.setText("-" + input);
            }
        }
    } catch (NumberFormatException ex) { // Handle NumberFormatException
    }
    }//GEN-LAST:event_SignActionPerformed

    private void ClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearAllActionPerformed
        InputOutput.setText("");
              decimalAdded = false;
        operate = false;
       set=0;
       if(set==0){
            Plus.setEnabled(true);
      }
    }//GEN-LAST:event_ClearAllActionPerformed

    private void InputOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputOutputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputOutputActionPerformed

    private void InputOutputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InputOutputKeyPressed

    }//GEN-LAST:event_InputOutputKeyPressed

    private void BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BActionPerformed
        InputOutput.setText(InputOutput.getText()+"B");
         Equation.setText(Equation.getText()+"B");
          
    }//GEN-LAST:event_BActionPerformed

    private void cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cActionPerformed
        h.setSelected(false);
        A.setEnabled(false);
        B.setEnabled(false);
        C.setEnabled(false);
        D.setEnabled(false);
        E.setEnabled(false);
        F.setEnabled(false);
        Sign.setEnabled(true);
        Minus.setEnabled(true);
        Divide.setEnabled(true);
        Times.setEnabled(true);
        Dot.setEnabled(true);
        InputOutput.setText("");
        call=0;
    }//GEN-LAST:event_cActionPerformed

    private void hActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hActionPerformed
        c.setSelected(false);
        A.setEnabled(true);
        B.setEnabled(true);
        C.setEnabled(true);
        D.setEnabled(true);
        E.setEnabled(true);
        F.setEnabled(true);
        Sign.setEnabled(false);
        Minus.setEnabled(false);
        Divide.setEnabled(false);
        Times.setEnabled(false);
        InputOutput.setText("");
        call=1;
    }//GEN-LAST:event_hActionPerformed

    private void TimesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimesActionPerformed
   if (!operate) {
        if(InputOutput.getText().equals("")){
           operate = false; 
        }else{
        InputOutput.setText(InputOutput.getText() + "*");
        operate = true;
        }
    }
        decimalAdded = false;
         
    }//GEN-LAST:event_TimesActionPerformed

    private void PlusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PlusKeyPressed
  
    }//GEN-LAST:event_PlusKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HexaAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HexaAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HexaAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HexaAdd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HexaAdd().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A;
    private javax.swing.JButton B;
    private javax.swing.JButton C;
    private javax.swing.JButton ClearAll;
    private javax.swing.JButton D;
    private javax.swing.JButton Delete;
    private javax.swing.JButton Divide;
    private javax.swing.JButton Dot;
    private javax.swing.JButton E;
    private javax.swing.JButton Eight;
    private javax.swing.JButton Equals;
    private javax.swing.JLabel Equation;
    private javax.swing.JButton F;
    private javax.swing.JButton Five;
    private javax.swing.JButton Four;
    private javax.swing.JTextField InputOutput;
    private javax.swing.JButton Minus;
    private javax.swing.JButton Nine;
    private javax.swing.JButton One;
    private javax.swing.JButton Plus;
    private javax.swing.JButton Seven;
    private javax.swing.JButton Sign;
    private javax.swing.JButton Six;
    private javax.swing.JButton Three;
    private javax.swing.JButton Times;
    private javax.swing.JButton Two;
    private javax.swing.JButton Zero;
    private javax.swing.JRadioButton c;
    private javax.swing.JRadioButton h;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
