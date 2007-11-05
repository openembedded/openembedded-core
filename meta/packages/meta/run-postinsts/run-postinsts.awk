#
# Copyright 2007 Openedhand Ltd.
#
# Author: Richard Purdie <rpurdie@openedhand.com>
#
# Rather hacky proof of concept
#

BEGIN {
  package=""
}
/Package:.*/ {
  package = substr($0, 10)
}
/Status:.*unpacked.*/ {
  print "Configuring: " package > "/dev/stderr"
  ret = system("/usr/lib/ipkg/info/" package ".postinst 1>&2")
  if (ret == 0)
    $0 = gensub("unpacked", "installed", 1)
  else
    print "Postinstall failed for " package > "/dev/stderr"
}
{
  print $0
}
