#!/bin/bash

#  Copyright (c) 2012 Wind River Systems, Inc.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
#

usage () {
  cat << EOF
Welcome to sstate cache management utilities.
sstate-cache-management.sh <OPTION>

Options:
  --help, -h
        Display this help and exit.

  --cache-dir=<sstate cache dir>
        Specify sstate cache directory, will use the environment
        variable SSTATE_CACHE_DIR if it is not specified.

  --remove-duplicated
        Remove the duplicated sstate cache files of one package, only
        the newest one would be kept.

EOF
}

if [ $# -lt 1 ]; then
  usage
  exit 0
fi

# Print error information and exit.
echo_error () {
  echo "ERROR: $1" >&2
  exit 1
}

# Remove the duplicated cache files for the pkg, keep the newest one
remove_duplicated () {
  local all_suffixes="$1"
  local ava_archs="$2"
  local total_deleted=0
  for suffix in $all_suffixes; do
      local deleted=0
      echo -n "Removing the sstate-xxx_$suffix.tgz ... "
      # sed twice to avoid the greedy match
      file_names=`for arch in $ava_archs; do
          ls sstate-*-$arch-*_$suffix.tgz 2>/dev/null | \
              sed -e 's/\(.*\)-'"$arch"'-.*/\1/' \
              -e 's/\(.*\)-'"$arch"'-.*/\1/'
      done | sort -u`

      for fn in $file_names; do
          for arch in $ava_archs; do
              ls $fn-$arch-*_$suffix.tgz 2>/dev/null >>/tmp/$fn
          done
          # Also delete the .siginfo file
          to_del=$(ls -t $(cat /tmp/$fn) | sed -n '1!p' | sed -e 'p' -e 's/$/.siginfo/')
          rm -f $to_del
          let deleted=$deleted+`echo $to_del | wc -w`
          rm -f /tmp/$fn
      done
      echo "($deleted files)"
      let total_deleted=$total_deleted+$deleted
  done
  echo "$total_deleted files have been removed"
}

# Parse arguments
while [ -n "$1" ]; do
  case $1 in
    --cache-dir=*)
      cache_dir=`echo $1 | sed -e 's#^--cache-dir=##' -e 's#/*$##' | xargs readlink -f`
      [ -d "$cache_dir" ] || echo_error "Invalid argument to --cache-dir"
      shift
        ;;
    --remove-duplicated)
      rm_duplicated="yes"
      shift
        ;;
    --help|-h)
      usage
      exit 0
        ;;
    *)
        echo "Invalid arguments $*"
        echo_error "Try 'sstate-cache-management.sh -h' for more information."
        ;;
  esac
done

# sstate cache directory, use environment variable SSTATE_CACHE_DIR
# if it was not specified, otherwise, error.
[ -n "$cache_dir" ] || cache_dir=$SSTATE_CACHE_DIR
[ -d "$cache_dir" ] || echo_error "Invalid cache directory \"$cache_dir\""

cache_dir=`readlink -f $cache_dir`

topdir=$(dirname $(dirname $(readlink -f $0)))
tunedir=$topdir/meta/conf/machine/include
[ -d $tunedir ] || echo_error "Can't find the tune directory"

# Use the "_" to substitute "-", e.g., x86-64 to x86_64
all_archs=`grep -r DEFAULTTUNE $tunedir | \
    sed -e 's/.*\"\(.*\)\"/\1/' -e 's/-/_/g' | sort -u`
# Add the qemu archs
all_archs="$all_archs qemuarm qemux86 qemumips qemuppc"

all_suffixes="deploy-rpm deploy-ipk deploy-deb deploy package populate-lic populate-sysroot"

cd $cache_dir

echo "Figuring out the archs in the sstate cache dir ..."
for arch in $all_archs; do
    ls | grep -q -w $arch
    [ $? -eq 0 ] && ava_archs="$ava_archs $arch"
done
echo "The following archs have been found in the sstate cache dir:"
echo $ava_archs

if [ "$rm_duplicated" == "yes" -a -n "$ava_archs" ]; then
    remove_duplicated "$all_suffixes" "$ava_archs"
fi
