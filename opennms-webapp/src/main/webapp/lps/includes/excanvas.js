var $dhtml=true
var $as3=false
var $js1=true
var $swf9=false
var $swf7=false
var $profile=false
var $swf8=false
var $runtime="dhtml"
var $svg=false
var $as2=false
var $debug=false
var $j2me=false
if(!window.CanvasRenderingContext2D){
(function(){
var onPropertyChange
var onResize
var createMatrixIdentity
var matrixMultiply
var copyState
var processStyle
var processLineCap
var CanvasRenderingContext2D_
var CanvasGradient_
var CanvasPattern_
onPropertyChange=function($1){
var $2=$1.srcElement
switch($1.propertyName){
case "width":
$2.style.width=$2.attributes.width.nodeValue+"px"
$2.getContext().clearRect()
break
case "height":
$2.style.height=$2.attributes.height.nodeValue+"px"
$2.getContext().clearRect()
break

}
}
onResize=function($1){
var $2=$1.srcElement
if($2.firstChild){
$2.firstChild.style.width=$2.clientWidth+"px"
$2.firstChild.style.height=$2.clientHeight+"px"
}
}
createMatrixIdentity=function(){
return [[1,0,0],[0,1,0],[0,0,1]]
}
matrixMultiply=function($1,$2){
var $3=createMatrixIdentity()
for(var $4=0;$4<3;$4++){
for(var $5=0;$5<3;$5++){
var $6=0
for(var $7=0;$7<3;$7++){
$6+=$1[$4][$7]*$2[$7][$5]
}
$3[$4][$5]=$6
}
}
return $3
}
copyState=function($1,$2){
$2.fillStyle=$1.fillStyle
$2.lineCap=$1.lineCap
$2.lineJoin=$1.lineJoin
$2.lineWidth=$1.lineWidth
$2.miterLimit=$1.miterLimit
$2.shadowBlur=$1.shadowBlur
$2.shadowColor=$1.shadowColor
$2.shadowOffsetX=$1.shadowOffsetX
$2.shadowOffsetY=$1.shadowOffsetY
$2.strokeStyle=$1.strokeStyle
$2.globalAlpha=$1.globalAlpha
$2.arcScaleX_=$1.arcScaleX_
$2.arcScaleY_=$1.arcScaleY_
}
processStyle=function($1){
var $2
var $3=1
$1=String($1)
if($1.substring(0,3)=="rgb"){
var $4=$1.indexOf("(",3)
var $5=$1.indexOf(")",$4+1)
var $6=$1.substring($4+1,$5).split(",")
$2="#"
for(var $7=0;$7<3;$7++){
$2+=dec2hex[Number($6[$7])]
}
if($6.length==4&&$1.substr(3,1)=="a"){
$3=$6[3]
}
}else{
$2=$1
}
return [$2,$3]
}
processLineCap=function($1){
switch($1){
case "butt":
return "flat"
case "round":
return "round"
case "square":
default:
return "square"

}
}
CanvasRenderingContext2D_=function($1){
this.m_=createMatrixIdentity()
this.mStack_=[]
this.aStack_=[]
this.currentPath_=[]
this.strokeStyle="#000"
this.fillStyle="#000"
this.lineWidth=1
this.lineJoin="miter"
this.lineCap="butt"
this.miterLimit=Z*1
this.globalAlpha=1
this.canvas=$1
var $2=$1.ownerDocument.createElement("div")
$2.style.width=$1.clientWidth+"px"
$2.style.height=$1.clientHeight+"px"
$2.style.overflow="hidden"
$2.style.position="absolute"
$1.appendChild($2)
this.element_=$2
this.arcScaleX_=1
this.arcScaleY_=1
}
CanvasGradient_=function($1){
this.type_=$1
this.radius1_=0
this.radius2_=0
this.colors_=[]
this.focus_={x:0,y:0}
}
CanvasPattern_=function(){

}
var $1=Math
var mr=$1.round
var ms=$1.sin
var mc=$1.cos
var Z=10
var Z2=Z/2
var $2={init:function($1){
var doc=$1||document
if(Lz.__BrowserDetect.isIE){
var self=this
doc.attachEvent("onreadystatechange",function(){
self.init_(doc)
})
}
},init_:function($1){
if($1.readyState=="complete"){
if(!$1.namespaces["g_vml_"]){
$1.namespaces.add("g_vml_","urn:schemas-microsoft-com:vml")
}
var $2=$1.createStyleSheet()
$2.cssText="canvas{display:inline-block;overflow:hidden;"+"text-align:left;width:300px;height:150px}"+"g_vml_\\:*{behavior:url(#default#VML)}"
var $3=$1.getElementsByTagName("canvas")
for(var $4=0;$4<$3.length;$4++){
if(!$3[$4].getContext){
this.initElement($3[$4])
}
}
}
},fixElement_:function($1){
var $2=$1.outerHTML
var $3=$1.ownerDocument.createElement($2)
if($2.slice(-2)!="/>"){
var $4="/"+$1.tagName
var $5
while(($5=$1.nextSibling)&&$5.tagName!=$4){
$5.removeNode()
}
if($5){
$5.removeNode()
}
}
$1.parentNode.replaceChild($3,$1)
return $3
},initElement:function($1){
$1=this.fixElement_($1)
$1.getContext=function(){
if(this.context_){
return this.context_
}
return this.context_=new CanvasRenderingContext2D_(this)
}
$1.attachEvent("onpropertychange",onPropertyChange)
$1.attachEvent("onresize",onResize)
var $2=$1.attributes
if($2.width&&$2.width.specified){
$1.style.width=$2.width.nodeValue+"px"
}else{
$1.width=$1.clientWidth
}
if($2.height&&$2.height.specified){
$1.style.height=$2.height.nodeValue+"px"
}else{
$1.height=$1.clientHeight
}
return $1
}}
$2.init()
var dec2hex=[]
for(var $3=0;$3<16;$3++){
for(var $4=0;$4<16;$4++){
dec2hex[$3*16+$4]=$3.toString(16)+$4.toString(16)
}
}
var $5=CanvasRenderingContext2D_.prototype
$5.clearRect=function(){
this.element_.innerHTML=""
this.currentPath_=[]
}
$5.beginPath=function(){
this.currentPath_=[]
}
$5.moveTo=function($1,$2){
this.currentPath_.push({type:"moveTo",x:$1,y:$2})
this.currentX_=$1
this.currentY_=$2
}
$5.lineTo=function($1,$2){
this.currentPath_.push({type:"lineTo",x:$1,y:$2})
this.currentX_=$1
this.currentY_=$2
}
$5.bezierCurveTo=function($1,$2,$3,$4,$5,$6){
this.currentPath_.push({type:"bezierCurveTo",cp1x:$1,cp1y:$2,cp2x:$3,cp2y:$4,x:$5,y:$6})
this.currentX_=$5
this.currentY_=$6
}
$5.quadraticCurveTo=function($1,$2,$3,$4){
var $5=this.currentX_+2/3*($1-this.currentX_)
var $6=this.currentY_+2/3*($2-this.currentY_)
var $7=$5+($3-this.currentX_)/3
var $8=$6+($4-this.currentY_)/3
this.bezierCurveTo($5,$6,$7,$8,$3,$4)
}
$5.arc=function($1,$2,$3,$4,$5,$6){
$3*=Z
var $7=$6?"at":"wa"
var $8=$1+mc($4)*$3-Z2
var $9=$2+ms($4)*$3-Z2
var $10=$1+mc($5)*$3-Z2
var $11=$2+ms($5)*$3-Z2
if($8==$10&&!$6){
$8+=0.125
}
this.currentPath_.push({type:$7,x:$1,y:$2,radius:$3,xStart:$8,yStart:$9,xEnd:$10,yEnd:$11})
}
$5.rect=function($1,$2,$3,$4){
this.moveTo($1,$2)
this.lineTo($1+$3,$2)
this.lineTo($1+$3,$2+$4)
this.lineTo($1,$2+$4)
this.closePath()
}
$5.strokeRect=function($1,$2,$3,$4){
this.beginPath()
this.moveTo($1,$2)
this.lineTo($1+$3,$2)
this.lineTo($1+$3,$2+$4)
this.lineTo($1,$2+$4)
this.closePath()
this.stroke()
}
$5.fillRect=function($1,$2,$3,$4){
this.beginPath()
this.moveTo($1,$2)
this.lineTo($1+$3,$2)
this.lineTo($1+$3,$2+$4)
this.lineTo($1,$2+$4)
this.closePath()
this.fill()
}
$5.createLinearGradient=function($1,$2,$3,$4){
var $5=new CanvasGradient_("gradient")
return $5
}
$5.createRadialGradient=function($1,$2,$3,$4,$5,$6){
var $7=new CanvasGradient_("gradientradial")
$7.radius1_=$3
$7.radius2_=$6
$7.focus_.x=$1
$7.focus_.y=$2
return $7
}
$5.drawImage=function($1,$2){
var $3
var $4
var $5
var $6
var $7
var $8
var $9
var $10
var $11=$1.runtimeStyle.width
var $12=$1.runtimeStyle.height
$1.runtimeStyle.width="auto"
$1.runtimeStyle.height="auto"
var $13=$1.width
var $14=$1.height
$1.runtimeStyle.width=$11
$1.runtimeStyle.height=$12
if(arguments.length==3){
$3=arguments[1]
$4=arguments[2]
$7=$8=0
$9=$5=$13
$10=$6=$14
}else{
if(arguments.length==5){
$3=arguments[1]
$4=arguments[2]
$5=arguments[3]
$6=arguments[4]
$7=$8=0
$9=$13
$10=$14
}else{
if(arguments.length==9){
$7=arguments[1]
$8=arguments[2]
$9=arguments[3]
$10=arguments[4]
$3=arguments[5]
$4=arguments[6]
$5=arguments[7]
$6=arguments[8]
}else{
throw "Invalid number of arguments"
}
}
}
var $15=this.getCoords_($3,$4)
var $16=$9/2
var $17=$10/2
var $18=[]
var $19=10
var $20=10
$18.push(" <g_vml_:group",' coordsize="',Z*$19,",",Z*$20,'"',' coordorigin="0,0"',' style="width:',$19,";height:",$20,";position:absolute;")
if(this.m_[0][0]!=1||this.m_[0][1]){
var $21=[]
$21.push("M11='",this.m_[0][0],"',","M12='",this.m_[1][0],"',","M21='",this.m_[0][1],"',","M22='",this.m_[1][1],"',","Dx='",mr($15.x/Z),"',","Dy='",mr($15.y/Z),"'")
var $22=$15
var $23=this.getCoords_($3+$5,$4)
var $24=this.getCoords_($3,$4+$6)
var $25=this.getCoords_($3+$5,$4+$6)
$22.x=Math.max($22.x,$23.x,$24.x,$25.x)
$22.y=Math.max($22.y,$23.y,$24.y,$25.y)
$18.push("padding:0 ",mr($22.x/Z),"px ",mr($22.y/Z),"px 0;filter:progid:DXImageTransform.Microsoft.Matrix(",$21.join(""),", sizingmethod='clip');")
}else{
$18.push("top:",mr($15.y/Z),"px;left:",mr($15.x/Z),"px;")
}
$18.push(' ">','<g_vml_:image src="',$1.src,'"',' style="width:',Z*$5,";"," height:",Z*$6,';"',' cropleft="',$7/$13,'"',' croptop="',$8/$14,'"',' cropright="',($13-$7-$9)/$13,'"',' cropbottom="',($14-$8-$10)/$14,'"'," />","</g_vml_:group>")
this.element_.insertAdjacentHTML("BeforeEnd",$18.join(""))
}
$5.stroke=function($1){
var $2=[]
var $3=false
var $4=processStyle($1?this.fillStyle:this.strokeStyle)
var $5=$4[0]
var $6=$4[1]*this.globalAlpha
var $7=10
var $8=10
$2.push("<g_vml_:shape",' fillcolor="',$5,'"',' filled="',Boolean($1),'"',' style="position:absolute;width:',$7,";height:",$8,';"',' coordorigin="0 0" coordsize="',Z*$7," ",Z*$8,'"',' stroked="',!$1,'"',' strokeweight="',this.lineWidth,'"',' strokecolor="',$5,'"',' path="')
var $9=false
var $10={x:null,y:null}
var $11={x:null,y:null}
for(var $12=0;$12<this.currentPath_.length;$12++){
var $13=this.currentPath_[$12]
if($13.type=="moveTo"){
$2.push(" m ")
var $14=this.getCoords_($13.x,$13.y)
$2.push(mr($14.x),",",mr($14.y))
}else{
if($13.type=="lineTo"){
$2.push(" l ")
var $14=this.getCoords_($13.x,$13.y)
$2.push(mr($14.x),",",mr($14.y))
}else{
if($13.type=="close"){
$2.push(" x ")
}else{
if($13.type=="bezierCurveTo"){
$2.push(" c ")
var $14=this.getCoords_($13.x,$13.y)
var $15=this.getCoords_($13.cp1x,$13.cp1y)
var $16=this.getCoords_($13.cp2x,$13.cp2y)
$2.push(mr($15.x),",",mr($15.y),",",mr($16.x),",",mr($16.y),",",mr($14.x),",",mr($14.y))
}else{
if($13.type=="at"||$13.type=="wa"){
$2.push(" ",$13.type," ")
var $14=this.getCoords_($13.x,$13.y)
var $17=this.getCoords_($13.xStart,$13.yStart)
var $18=this.getCoords_($13.xEnd,$13.yEnd)
$2.push(mr($14.x-this.arcScaleX_*$13.radius),",",mr($14.y-this.arcScaleY_*$13.radius)," ",mr($14.x+this.arcScaleX_*$13.radius),",",mr($14.y+this.arcScaleY_*$13.radius)," ",mr($17.x),",",mr($17.y)," ",mr($18.x),",",mr($18.y))
}
}
}
}
}
if($14){
if($10.x==null||$14.x<$10.x){
$10.x=$14.x
}
if($11.x==null||$14.x>$11.x){
$11.x=$14.x
}
if($10.y==null||$14.y<$10.y){
$10.y=$14.y
}
if($11.y==null||$14.y>$11.y){
$11.y=$14.y
}
}
}
$2.push(' ">')
if(typeof this.fillStyle=="object"){
var $19={x:"50%",y:"50%"}
var $20=$11.x-$10.x
var $21=$11.y-$10.y
var $22=$20>$21?$20:$21
$19.x=mr(this.fillStyle.focus_.x/$20*100+50)+"%"
$19.y=mr(this.fillStyle.focus_.y/$21*100+50)+"%"
var $23=[]
if(this.fillStyle.type_=="gradientradial"){
var $24=this.fillStyle.radius1_/$22*100
var $25=this.fillStyle.radius2_/$22*100-$24
}else{
var $24=0
var $25=100
}
var $26={offset:null,color:null}
var $27={offset:null,color:null}
this.fillStyle.colors_.sort(function($1,$2){
return $1.offset-$2.offset
})
for(var $12=0;$12<this.fillStyle.colors_.length;$12++){
var $28=this.fillStyle.colors_[$12]
$23.push($28.offset*$25+$24,"% ",$28.color,",")
if($28.offset>$26.offset||$26.offset==null){
$26.offset=$28.offset
$26.color=$28.color
}
if($28.offset<$27.offset||$27.offset==null){
$27.offset=$28.offset
$27.color=$28.color
}
}
$23.pop()
$2.push("<g_vml_:fill",' color="',$27.color,'"',' color2="',$26.color,'"',' type="',this.fillStyle.type_,'"',' focusposition="',$19.x,", ",$19.y,'"',' colors="',$23.join(""),'"',' opacity="',$6,'" />')
}else{
if($1){
$2.push('<g_vml_:fill color="',$5,'" opacity="',$6,'" />')
}else{
$2.push("<g_vml_:stroke",' opacity="',$6,'"',' joinstyle="',this.lineJoin,'"',' miterlimit="',this.miterLimit,'"',' endcap="',processLineCap(this.lineCap),'"',' weight="',this.lineWidth,'px"',' color="',$5,'" />')
}
}
$2.push("</g_vml_:shape>")
this.element_.insertAdjacentHTML("beforeEnd",$2.join(""))
this.currentPath_=[]
}
$5.fill=function(){
this.stroke(true)
}
$5.closePath=function(){
this.currentPath_.push({type:"close"})
}
$5.getCoords_=function($1,$2){
return {x:Z*($1*this.m_[0][0]+$2*this.m_[1][0]+this.m_[2][0])-Z2,y:Z*($1*this.m_[0][1]+$2*this.m_[1][1]+this.m_[2][1])-Z2}
}
$5.save=function(){
var $1={}
copyState(this,$1)
this.aStack_.push($1)
this.mStack_.push(this.m_)
this.m_=matrixMultiply(createMatrixIdentity(),this.m_)
}
$5.restore=function(){
copyState(this.aStack_.pop(),this)
this.m_=this.mStack_.pop()
}
$5.translate=function($1,$2){
var $3=[[1,0,0],[0,1,0],[$1,$2,1]]
this.m_=matrixMultiply($3,this.m_)
}
$5.rotate=function($1){
var $2=mc($1)
var $3=ms($1)
var $4=[[$2,$3,0],[-$3,$2,0],[0,0,1]]
this.m_=matrixMultiply($4,this.m_)
}
$5.scale=function($1,$2){
this.arcScaleX_*=$1
this.arcScaleY_*=$2
var $3=[[$1,0,0],[0,$2,0],[0,0,1]]
this.m_=matrixMultiply($3,this.m_)
}
$5.clip=function(){

}
$5.arcTo=function(){

}
$5.createPattern=function(){
return new CanvasPattern_()
}
CanvasGradient_.prototype.addColorStop=function($1,$2){
$2=processStyle($2)
this.colors_.push({offset:1-$1,color:$2})
}
G_vmlCanvasManager=$2
CanvasRenderingContext2D=CanvasRenderingContext2D_
CanvasGradient=CanvasGradient_
CanvasPattern=CanvasPattern_
})()
}
