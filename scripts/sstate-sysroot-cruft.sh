#!/bin/sh

# Used to find files installed in sysroot which are not tracked by sstate manifest

# Global vars
tmpdir=

usage () {
  cat << EOF
Welcome to sysroot cruft finding utility.
$0 <OPTION>

Options:
  -h, --help
        Display this help and exit.

  --tmpdir=<tmpdir>
        Specify tmpdir, will use the environment variable TMPDIR if it is not specified.
	Something like /OE/oe-core/tmp-eglibc (no / at the end).
EOF
}

# Print error information and exit.
echo_error () {
  echo "ERROR: $1" >&2
  exit 1
}

while [ -n "$1" ]; do
  case $1 in
    --tmpdir=*)
      tmpdir=`echo $1 | sed -e 's#^--tmpdir=##' | xargs readlink -e`
      [ -d "$tmpdir" ] || echo_error "Invalid argument to --tmpdir"
      shift
        ;;
    --help|-h)
      usage
      exit 0
        ;;
    *)
      echo "Invalid arguments $*"
      echo_error "Try '$0 -h' for more information."
        ;;
  esac
done

# sstate cache directory, use environment variable TMPDIR
# if it was not specified, otherwise, error.
[ -n "$tmpdir" ] || tmpdir=$TMPDIR
[ -n "$tmpdir" ] || echo_error "No tmpdir found!"
[ -d "$tmpdir" ] || echo_error "Invalid tmpdir \"$tmpdir\""

OUTPUT=${tmpdir}/sysroot.cruft.`date "+%s"`
WHITELIST="\/var\/pseudo\($\|\/[^\/]*$\) \/shlibs$ \.pyc$ \.pyo$"

mkdir ${OUTPUT}
find ${tmpdir}/sstate-control -name \*.populate-sysroot\* -o -name \*.package\* | xargs cat | grep sysroots | \
  sed 's#/$##g; s#///*#/#g' | \
  # work around for paths ending with / for directories and multiplied // (e.g. paths to native sysroot)
  sort > ${OUTPUT}/master.list.all
sort -u ${OUTPUT}/master.list.all > ${OUTPUT}/master.list # -u because some directories are listed for more recipes
find ${tmpdir}/sysroots/ | \
  sort > ${OUTPUT}/sysroot.list

diff ${OUTPUT}/master.list.all ${OUTPUT}/master.list > ${OUTPUT}/duplicates
diff ${OUTPUT}/master.list ${OUTPUT}/sysroot.list > ${OUTPUT}/diff.all

cp ${OUTPUT}/diff.all ${OUTPUT}/diff
for item in ${WHITELIST}; do
  sed -i "/${item}/d" ${OUTPUT}/diff;
done

# too many false positives for directories
# echo "Following files are installed in sysroot at least twice"
# cat ${OUTPUT}/duplicates

echo "Following files are installed in sysroot, but not tracked by sstate"
cat ${OUTPUT}/diff
