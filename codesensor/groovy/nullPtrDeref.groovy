
import java.util.regex.Pattern

import CodeDatabase
c = new CodeDatabase()
g = c.getDbLink()

def lvals = []

// All assignments to malloc

mallocAssigns  = astNodes("AssignmentExpr").filter{it.code.matches('.*[^_]malloc.*')}.as('assign')

// functions containing these assignments

r = mallocAssigns.function().sideEffect
{
  filename = it.filename;
  signature = it.signature
}.back('assign')

// lvals of assignments

r = r.lval().sideEffect
{
  lval = it.code;
}.back('assign')

// paths to exit from assignment

paths = r.basicBlock().pathsToExit().transform
{
  ret = []
  it.each()
  {
    if(it.code.matches('.*' + Pattern.quote(lval) + ".*"))
      ret << [it.type, it.code]   
  }
  [ret, lval, filename, signature];
}.toList()


for (path in paths)
{
  typeCodeList = path[0]
  lval = path[1]
  filename = path[2]
  signature = path[3]
  
  if(typeCodeList.size() > 0 && typeCodeList[0][0] == 'ConditionBB')
    continue;

  typeCodeList.remove(0);

  for (elem in typeCodeList){
    
    type = elem[0]
    
    if(type != "ConditionBB"){
      println filename + " " + signature + " " + lval
      break;
    }
    break;
  }
  
}

g.shutdown();
