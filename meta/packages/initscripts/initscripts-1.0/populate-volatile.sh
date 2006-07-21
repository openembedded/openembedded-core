#!/bin/sh

. /etc/default/rcS

CFGDIR="/etc/default/volatiles"
TMPROOT="/var/tmp"
COREDEF="00_core"

[ "${VERBOSE}" != "no" ] && echo "Populating volatile Filesystems."


check_requirements() {

  cleanup() {
    rm "${TMP_INTERMED}"
    rm "${TMP_DEFINED}"
    rm "${TMP_COMBINED}"
    }
    
  CFGFILE="$1"

  [ `basename "${CFGFILE}"` = "${COREDEF}" ] && return 0

  TMP_INTERMED="${TMPROOT}/tmp.$$"
  TMP_DEFINED="${TMPROOT}/tmpdefined.$$"
  TMP_COMBINED="${TMPROOT}/tmpcombined.$$"


  cat /etc/passwd | sed 's@\(^:\)*:.*@\1@' | sort | uniq > "${TMP_DEFINED}"
  cat ${CFGFILE} | grep -v "^#" | cut -d " " -f 2 > "${TMP_INTERMED}"
  cat "${TMP_DEFINED}" "${TMP_INTERMED}" | sort | uniq > "${TMP_COMBINED}"

  NR_DEFINED_USERS="`cat "${TMP_DEFINED}" | wc -l`"
  NR_COMBINED_USERS="`cat "${TMP_COMBINED}" | wc -l`"

  [ "${NR_DEFINED_USERS}" -ne "${NR_COMBINED_USERS}" ] && {
    echo "Undefined users:"
    diff "${TMP_DEFINED}" "${TMP_COMBINED}" | grep "^>"
    cleanup
    return 1
    }


  cat /etc/group | sed 's@\(^:\)*:.*@\1@' | sort | uniq > "${TMP_DEFINED}"
  cat ${CFGFILE} | grep -v "^#" | cut -d " " -f 3 > "${TMP_INTERMED}"
  cat "${TMP_DEFINED}" "${TMP_INTERMED}" | sort | uniq > "${TMP_COMBINED}"

  NR_DEFINED_GROUPS="`cat "${TMP_DEFINED}" | wc -l`"
  NR_COMBINED_GROUPS="`cat "${TMP_COMBINED}" | wc -l`"

  [ "${NR_DEFINED_GROUPS}" -ne "${NR_COMBINED_GROUPS}" ] && {
    echo "Undefined groups:"
    diff "${TMP_DEFINED}" "${TMP_COMBINED}" | grep "^>"
    cleanup
    return 1
    }

  # Add checks for required directories here

  cleanup
  return 0
  }

apply_cfgfile() {

  CFGFILE="$1"

  check_requirements "${CFGFILE}" || {
    echo "Skipping ${CFGFILE}"
    return 1
    }

  cat ${CFGFILE} | grep -v "^#" | \
  while read LINE; do
    TTYPE=`echo ${LINE} | cut -d " " -f 1`
    TUSER=`echo ${LINE} | cut -d " " -f 2`
    TGROUP=`echo ${LINE} | cut -d " " -f 3`
    TMODE=`echo ${LINE} | cut -d " " -f 4`
    TNAME=`echo ${LINE} | cut -d " " -f 5`

    [ "${VERBOSE}" != "no" ] && echo "Checking for -${TNAME}-."

    [ "${TTYPE}" = "l" ] && {
      [ -e "${TNAME}" ] && {
        echo "Cannot create link over existing -${TNAME}-." >&2
        } || {
        TSOURCE=`echo ${LINE} | cut -d " " -f 6`
        [ "${VERBOSE}" != "no" ] && echo "Creating link -${TNAME}- pointing to -${TSOURCE}-."
        ln -s "${TSOURCE}" "${TNAME}"
        }
      continue
      }

    [ -L "${TNAME}" ] && {
      [ "${VERBOSE}" != "no" ] && echo "Found link."
      NEWNAME=`ls -l "${TNAME}" | sed -e 's/^.*-> \(.*\)$/\1/'`
      echo ${NEWNAME} | grep -v "^/" >/dev/null && {
        TNAME="`echo ${TNAME} | sed -e 's@\(.*\)/.*@\1@'`/${NEWNAME}"
        [ "${VERBOSE}" != "no" ] && echo "Converted relative linktarget to absolute path -${TNAME}-."
        } || {
        TNAME="${NEWNAME}"
        [ "${VERBOSE}" != "no" ] && echo "Using absolute link target -${TNAME}-."
        }
      }

    [ -e "${TNAME}" ] && {
      [ "${VERBOSE}" != "no" ] && echo "Target already exists. Skipping."
      continue
      }

    case "${TTYPE}" in
      "f")  [ "${VERBOSE}" != "no" ] && echo "Creating file -${TNAME}-."
            touch "${TNAME}"
	    ;;
      "d")  [ "${VERBOSE}" != "no" ] && echo "Creating directory -${TNAME}-."
            mkdir -p "${TNAME}"
	    # Add check to see if there's an entry in fstab to mount.
	    ;;
      *)    [ "${VERBOSE}" != "no" ] && echo "Invalid type -${TTYPE}-."
            continue
	    ;;
    esac

    chown ${TUSER} ${TNAME} || echo "Failed to set owner -${TUSER}- for -${TNAME}-." >&2
    chgrp ${TGROUP} ${TNAME} || echo "Failed to set group -${TGROUP}- for -${TNAME}-." >&2
    chmod ${TMODE} ${TNAME} || echo "Failed to set mode -${TMODE}- for -${TNAME}-." >&2

    done

  return 0

  }


for file in `ls -1 "${CFGDIR}" | sort`; do
  apply_cfgfile "${CFGDIR}/${file}"
  done

