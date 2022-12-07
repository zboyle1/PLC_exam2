# PLC_exam2
## Grammar for undefined language -- trees language
### Parse table
[Parse table](https://htmlpreview.github.io/?https://github.com/zboyle1/PLC_exam2/blob/main/parsetable.html)
<br>[Accepted trace 1](https://htmlpreview.github.io/?https://github.com/zboyle1/PLC_exam2/blob/main/correct_trace1.html)
<br>[Accepted trace 2](https://htmlpreview.github.io/?https://github.com/zboyle1/PLC_exam2/blob/main/correct_trace2.html)
<br>[Failed trace 1](https://htmlpreview.github.io/?https://github.com/zboyle1/PLC_exam2/blob/main/failed_trace1.html)
<br>[Failed trace 2](https://htmlpreview.github.io/?https://github.com/zboyle1/PLC_exam2/blob/main/failed_trace2.html)
### RegEx
<br>([a-z]_?){6,8} RegEx for variable names
<br>[0-9]+ RegEx for numbers

### Grammar
- S = \<program>
- V = { 
    <br>\<program>, \<stmt>, \<ifstmt>, \<forloop>, \<whileloop>,
		<br>\<switch>, \<case>, \<expr>, \<assertion>, \<term>, 
		<br>\<boolexpr>, \<var>, \<ident>, \<digit>, \<elsestmt>,
		<br>\<declaration>
	<br>}
- ∑ = { 
    <br>tree, leaf, seed, twig, trunk, stick, apple,
		<br>orange, climb, branch, growfor, grow, root, eat, stop,
		<br><, >, <=, >=, ==, !=, (, ), {, }, ,, =, +, -, *, /,
		<br>%, a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,
		<br>q, r, s, t, u, v, w, x, y, z, _, 0, 1, 2, 3, 4, 5,
		<br>6, 7, 8, 9
	<br>}
- R = [
		<br><program> --> tree{ <stmt> }
		<br><stmt> --> <forloop><stmt>|<whileloop><stmt>|<var> leaf <stmt>|<ifstmt><stmt>|<switch><stmt>|<dowhile><stmt>|<assertion> leaf <stmt>|null
		<br><forloop> --> growfor(<var>,<boolexpr>,<expr>) <stmt> stop
		<br><whileloop> --> grow(<boolexpr>) <stmt> stop
		<br><dowhile> --> root <stmt> stop grow(<boolexpr>)
		<br><switch> --> climb(<ident>) <case> stop
		<br><case> --> branch <digit>: <stmt> <case>|null
		<br><ifstmt> --> apple(<boolexpr>) <stmt> <elsestmt> eat
		<br><elsestmt> --> orange <stmt>|null
		<br><var> --> <type> <ident> <declaration>
		<br><declaration> --> = <expr>|null
		<br><assertion> --> <ident> = <expr>
		<br><type> --> seed|stick|twig|trunk
		<br><expr> --> <term>|(<expr>)
		<br><term> --> <ident>|<digit>|+ <expr>|- <expr>|* <expr>|/ <expr>|% <expr>
		<br><boolexpr> --> <expr> <op> <expr>
		<br><op> --> >|>=|<|<=|==|!=
		<br><ident> = <letter>|_<letter>
		<br><letter> --> a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z
		<br><digit> --> 0|1|2|3|4|5|6|7|8|9
	<br>]
