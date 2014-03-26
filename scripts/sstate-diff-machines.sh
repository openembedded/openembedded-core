#!/bin/sh

# Used to compare sstate checksums between MACHINES
# Execute script and compare generated list.M files

# It's also usefull to keep older sstate checksums
# to be able to find out why something is rebuilding
# after updating metadata

# $ diff \
#     sstate-diff/1349348392/fake-cortexa8/list.M \
#     sstate-diff/1349348392/fake-cortexa9/list.M \
#     | wc -l
# 538

# Then to compare sigdata use something like:
# $ ls sstate-diff/1349348392/*/armv7a-vfp-neon*/linux-libc-headers/*do_configure*sigdata*
#   sstate-diff/1349348392/fake-cortexa8/armv7a-vfp-neon-oe-linux-gnueabi/linux-libc-headers/3.4.3-r0.do_configure.sigdata.cb73b3630a7b8191e72fc469c5137025
#   sstate-diff/1349348392/fake-cortexa9/armv7a-vfp-neon-oe-linux-gnueabi/linux-libc-headers/3.4.3-r0.do_configure.sigdata.f37ada177bf99ce8af85914df22b5a0b
# $ bitbake-diffsigs stamps.1349348392/*/armv7a-vfp-neon*/linux-libc-headers/*do_configure*sigdata*
#   basehash changed from 8d0bd67bb1da6f68717760fc3ef43171 to e869fa61426e88e9c30726ba88a1216a
#   Variable TUNE_CCARGS value changed from  -march=armv7-a     -mthumb-interwork -mfloat-abi=softfp -mfpu=neon -mtune=cortex-a8 to  -march=armv7-a     -mthumb-interwork -mfloat-abi=softfp -mfpu=neon -mtune=cortex-a9

# Global vars
tmpdir=
machines=
targets=
default_machines="qemuarm qemux86 qemux86-64"
default_targets="core-image-base"

usage () {
  cat << EOF
Welcome to utility to compare sstate checksums between different MACHINEs.
$0 <OPTION>

Options:
  -h, --help
        Display this help and exit.

  --tmpdir=<tmpdir>
        Specify tmpdir, will use the environment variable TMPDIR if it is not specified.
        Something like /OE/oe-core/tmp-eglibc (no / at the end).

  --machines=<machines>
        List of MACHINEs separated by space, will use the environment variable MACHINES if it is not specified.
        Default value is "qemuarm qemux86 qemux86-64".

  --targets=<targets>
        List of targets separated by space, will use the environment variable TARGETS if it is not specified.
        Default value is "core-image-base".
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
    --machines=*)
      machines=`echo $1 | sed -e 's#^--machines="*\([^"]*\)"*#\1#'`
      shift
        ;;
    --targets=*)
      targets=`echo $1 | sed -e 's#^--targets="*\([^"]*\)"*#\1#'`
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

# tmpdir directory, use environment variable TMPDIR
# if it was not specified, otherwise, error.
[ -n "$tmpdir" ] || tmpdir=$TMPDIR
[ -n "$tmpdir" ] || echo_error "No tmpdir found!"
[ -d "$tmpdir" ] || echo_error "Invalid tmpdir \"$tmpdir\""
[ -n "$machines" ] || machines=$MACHINES
[ -n "$machines" ] || machines=$default_machines
[ -n "$targets" ] || targets=$TARGETS
[ -n "$targets" ] || targets=$default_targets

OUTPUT=${tmpdir}/sstate-diff/`date "+%s"`

for M in ${machines}; do
  find ${tmpdir}/stamps/ -name \*sigdata\* | xargs rm -f
  mkdir -p ${OUTPUT}/${M}
  export MACHINE=${M}; bitbake -S none ${targets} | tee -a ${OUTPUT}/${M}/log;
  cp -ra ${tmpdir}/stamps/* ${OUTPUT}/${M}
  find ${OUTPUT}/${M} -name \*sigdata\* | sed "s#${OUTPUT}/${M}/##g" | sort > ${OUTPUT}/${M}/list
  M_UNDERSCORE=`echo ${M} | sed 's/-/_/g'`
  sed "s/${M_UNDERSCORE}/MACHINE/g; s/${M}/MACHINE/g" ${OUTPUT}/${M}/list | sort > ${OUTPUT}/${M}/list.M
  find ${tmpdir}/stamps/ -name \*sigdata\* | xargs rm -f
done
