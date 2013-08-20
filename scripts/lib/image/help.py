# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# Copyright (c) 2013, Intel Corporation.
# All rights reserved.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
# DESCRIPTION
# This module implements some basic help invocation functions along
# with the bulk of the help topic text for the OE Core Image Tools.
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

import subprocess
import logging


def subcommand_error(args):
    logging.info("invalid subcommand %s" % args[0])


def display_help(subcommand, subcommands):
    """
    Display help for subcommand.
    """
    if subcommand not in subcommands:
        return False

    help = subcommands.get(subcommand, subcommand_error)[2]
    pager = subprocess.Popen('less', stdin=subprocess.PIPE)
    pager.communicate(help)

    return True


def wic_help(args, usage_str, subcommands):
    """
    Subcommand help dispatcher.
    """
    if len(args) == 1 or not display_help(args[1], subcommands):
        print(usage_str)


def invoke_subcommand(args, parser, main_command_usage, subcommands):
    """
    Dispatch to subcommand handler borrowed from combo-layer.
    Should use argparse, but has to work in 2.6.
    """
    if not args:
        logging.error("No subcommand specified, exiting")
        parser.print_help()
    elif args[0] == "help":
        wic_help(args, main_command_usage, subcommands)
    elif args[0] not in subcommands:
        logging.error("Unsupported subcommand %s, exiting\n" % (args[0]))
        parser.print_help()
    else:
        usage = subcommands.get(args[0], subcommand_error)[1]
        subcommands.get(args[0], subcommand_error)[0](args[1:], usage)


##
# wic help and usage strings
##

wic_usage = """

 Create a customized OpenEmbedded image

 usage: wic [--version] [--help] COMMAND [ARGS]

 Current 'wic' commands are:
    create            Create a new OpenEmbedded image
    list              List available values for options and image properties

 See 'wic help COMMAND' for more information on a specific command.
"""

wic_help_usage = """

 usage: wic help <subcommand>

 This command displays detailed help for the specified subcommand.
"""

wic_create_usage = """

 Create a new OpenEmbedded image

 usage: wic create <wks file or image name> [-o <DIRNAME> | --outdir <DIRNAME>]
            [-i <JSON PROPERTY FILE> | --infile <JSON PROPERTY_FILE>]
            [-e | --image-name] [-r, --rootfs-dir] [-b, --bootimg-dir]
            [-k, --kernel-dir] [-n, --native-sysroot] [-s, --skip-build-check]

 This command creates an OpenEmbedded image based on the 'OE kickstart
 commands' found in the <wks file>.

 The -o option can be used to place the image in a directory with a
 different name and location.

 See 'wic help create' for more detailed instructions.
"""

wic_create_help = """

NAME
    wic create - Create a new OpenEmbedded image

SYNOPSIS
    wic create <wks file or image name> [-o <DIRNAME> | --outdir <DIRNAME>]
        [-i <JSON PROPERTY FILE> | --infile <JSON PROPERTY_FILE>]
        [-e | --image-name] [-r, --rootfs-dir] [-b, --bootimg-dir]
        [-k, --kernel-dir] [-n, --native-sysroot] [-s, --skip-build-check]

DESCRIPTION
    This command creates an OpenEmbedded image based on the 'OE
    kickstart commands' found in the <wks file>.

    In order to do this, wic needs to know the locations of the
    various build artifacts required to build the image.

    Users can explicitly specify the build artifact locations using
    the -r, -b, -k, and -n options.  See below for details on where
    the corresponding artifacts are typically found in a normal
    OpenEmbedded build.

    Alternatively, users can use the -e option to have 'mic' determine
    those locations for a given image.  If the -e option is used, the
    user needs to have set the appropriate MACHINE variable in
    local.conf, and have sourced the build environment.

    The -e option is used to specify the name of the image to use the
    artifacts from e.g. core-image-sato.

    The -r option is used to specify the path to the /rootfs dir to
    use as the .wks rootfs source.

    The -b option is used to specify the path to the dir containing
    the boot artifacts (e.g. /EFI or /syslinux dirs) to use as the
    .wks bootimg source.

    The -k option is used to specify the path to the dir containing
    the kernel to use in the .wks bootimg.

    The -n option is used to specify the path to the native sysroot
    containing the tools to use to build the image.

    The -s option is used to skip the build check.  The build check is
    a simple sanity check used to determine whether the user has
    sourced the build environment so that the -e option can operate
    correctly.  If the user has specified the build artifact locations
    explicitly, 'wic' assumes the user knows what he or she is doing
    and skips the build check.

    When 'wic -e' is used, the locations for the build artifacts
    values are determined by 'wic -e' from the output of the 'bitbake
    -e' command given an image name e.g. 'core-image-minimal' and a
    given machine set in local.conf.  In that case, the image is
    created as if the following 'bitbake -e' variables were used:

    -r:        IMAGE_ROOTFS
    -k:        STAGING_KERNEL_DIR
    -n:        STAGING_DIR_NATIVE
    -b:        HDDDIR and STAGING_DATA_DIR (handlers decide which to use)

    If 'wic -e' is not used, the user needs to select the appropriate
    value for -b (as well as -r, -k, and -n).

    The -o option can be used to place the image in a directory with a
    different name and location.

    As an alternative to the wks file, the image-specific properties
    that define the values that will be used to generate a particular
    image can be specified on the command-line using the -i option and
    supplying a JSON object consisting of the set of name:value pairs
    needed by image creation.

    The set of properties available for a given image type can be
    listed using the 'wic list' command.
"""

wic_list_usage = """

 List available OpenEmbedded image properties and values

 usage: wic list images
        wic list <image> help
        wic list properties
        wic list properties <wks file>
        wic list property <property>
                [-o <JSON PROPERTY FILE> | --outfile <JSON PROPERTY_FILE>]

 This command enumerates the set of available canned images as well as
 help for those images.  It also can be used to enumerate the complete
 set of possible values for a specified option or property needed by
 the image creation process.

 The first form enumerates all the available 'canned' images.

 The second form lists the detailed help information for a specific
 'canned' image.

 The third form enumerates all the possible values that exist and can
 be specified in an OE kickstart (wks) file.

 The fourth form enumerates all the possible options that exist for
 the set of properties specified in a given OE kickstart (ks) file.

 The final form enumerates all the possible values that exist and can
 be specified for any given OE kickstart (wks) property.

 See 'wic help list' for more details.
"""

wic_list_help = """

NAME
    wic list - List available OpenEmbedded image properties and values

SYNOPSIS
    wic list images
    wic list <image> help
    wic list properties
    wic list properties <wks file>
    wic list property <property>
            [-o <JSON PROPERTY FILE> | --outfile <JSON PROPERTY_FILE>]

DESCRIPTION
    This command enumerates the complete set of possible values for a
    specified option or property needed by the image creation process.

    This command enumerates the set of available canned images as well
    as help for those images.  It also can be used to enumerate the
    complete set of possible values for a specified option or property
    needed by the image creation process.

    The first form enumerates all the available 'canned' images.
    These are actually just the set of .wks files that have been moved
    into the /scripts/lib/image/canned-wks directory).

    The second form lists the detailed help information for a specific
    'canned' image.

    The third form enumerates all the possible values that exist and
    can be specified in a OE kickstart (wks) file.  The output of this
    can be used by the third form to print the description and
    possible values of a specific property.

    The fourth form enumerates all the possible options that exist for
    the set of properties specified in a given OE kickstart (wks)
    file.  If the -o option is specified, the list of properties, in
    addition to being displayed, will be written to the specified file
    as a JSON object.  In this case, the object will consist of the
    set of name:value pairs corresponding to the (possibly nested)
    dictionary of properties defined by the input statements used by
    the image.  Some example output for the 'list <wks file>' command:

    $ wic list test.ks
    "part" : {
        "mountpoint" : "/"
        "fstype" : "ext3"
    }
    "part" : {
        "mountpoint" : "/home"
        "fstype" : "ext3"
        "offset" : "10000"
    }
    "bootloader" : {
        "type" : "efi"
    }
    .
    .
    .

    Each entry in the output consists of the name of the input element
    e.g. "part", followed by the properties defined for that
    element enclosed in braces.  This information should provide
    sufficient information to create a complete user interface with.

    The final form enumerates all the possible values that exist and
    can be specified for any given OE kickstart (wks) property.  If
    the -o option is specified, the list of values for the given
    property, in addition to being displayed, will be written to the
    specified file as a JSON object.  In this case, the object will
    consist of the set of name:value pairs corresponding to the array
    of property values associated with the property.

    $ wic list property part
        ["mountpoint", "where the partition should be mounted"]
        ["fstype", "filesytem type of the partition"]
            ["ext3"]
            ["ext4"]
            ["btrfs"]
            ["swap"]
        ["offset", "offset of the partition within the image"]

"""
