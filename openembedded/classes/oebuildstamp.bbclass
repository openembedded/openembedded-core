#
# Because base.oeclasses set the variable
#
#	do_fetch[nostamp] = "1"
#	do_build[nostamp] = "1"
#
# for every build we're doing oemake calls all of the phases to check if
# something new is to download. This class unset's this nostamp flag. This
# makes a package "finished", once it's completely build.
#
# This means that the subsequent builds are faster, but when you change the
# behaviour of the package, e.g. by adding INHERIT="package_ipk", you won't
# get the ipk file except you delete the build stamp manually or all of them
# with oebuild clean <oe-file>.

do_build[nostamp] = ""
