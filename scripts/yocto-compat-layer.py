#!/usr/bin/env python3

# Yocto Project compatibility layer tool
#
# Copyright (C) 2017 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os
import sys
import argparse
import logging
import time
import signal
import shutil
import collections

scripts_path = os.path.dirname(os.path.realpath(__file__))
lib_path = scripts_path + '/lib'
sys.path = sys.path + [lib_path]
import scriptutils
import scriptpath
scriptpath.add_oe_lib_path()
scriptpath.add_bitbake_lib_path()

from compatlayer import LayerType, detect_layers, add_layer, get_signatures
from oeqa.utils.commands import get_bb_vars

PROGNAME = 'yocto-compat-layer'
CASES_PATHS = [os.path.join(os.path.abspath(os.path.dirname(__file__)),
                'lib', 'compatlayer', 'cases')]
logger = scriptutils.logger_create(PROGNAME, stream=sys.stdout)

def test_layer_compatibility(td, layer):
    from compatlayer.context import CompatLayerTestContext
    logger.info("Starting to analyze: %s" % layer['name'])
    logger.info("----------------------------------------------------------------------")

    tc = CompatLayerTestContext(td=td, logger=logger, layer=layer)
    tc.loadTests(CASES_PATHS)
    return tc.runTests()

def main():
    parser = argparse.ArgumentParser(
            description="Yocto Project compatibility layer tool",
            add_help=False)
    parser.add_argument('layers', metavar='LAYER_DIR', nargs='+',
            help='Layer to test compatibility with Yocto Project')
    parser.add_argument('-o', '--output-log',
            help='File to output log (optional)', action='store')
    parser.add_argument('-n', '--no-auto', help='Disable auto layer discovery',
            action='store_true')
    parser.add_argument('-d', '--debug', help='Enable debug output',
            action='store_true')
    parser.add_argument('-q', '--quiet', help='Print only errors',
            action='store_true')

    parser.add_argument('-h', '--help', action='help',
            default=argparse.SUPPRESS,
            help='show this help message and exit')

    args = parser.parse_args()

    if args.output_log:
        fh = logging.FileHandler(args.output_log)
        fh.setFormatter(logging.Formatter("%(levelname)s: %(message)s"))
        logger.addHandler(fh)
    if args.debug:
        logger.setLevel(logging.DEBUG)
    elif args.quiet:
        logger.setLevel(logging.ERROR)

    if not 'BUILDDIR' in os.environ:
        logger.error("You must source the environment before run this script.")
        logger.error("$ source oe-init-build-env")
        return 1
    builddir = os.environ['BUILDDIR']
    bblayersconf = os.path.join(builddir, 'conf', 'bblayers.conf')

    layers = detect_layers(args.layers, args.no_auto)
    if not layers:
        logger.error("Fail to detect layers")
        return 1

    logger.info("Detected layers:")
    for layer in layers:
        if layer['type'] == LayerType.ERROR_BSP_DISTRO:
            logger.error("%s: Can't be DISTRO and BSP type at the same time."\
                     " The conf/distro and conf/machine folders was found."\
                     % layer['name'])
            layers.remove(layer)
        elif layer['type'] == LayerType.ERROR_NO_LAYER_CONF:
            logger.error("%s: Don't have conf/layer.conf file."\
                     % layer['name'])
            layers.remove(layer)
        else:
            logger.info("%s: %s, %s" % (layer['name'], layer['type'],
                layer['path']))
    if not layers:
        return 1

    shutil.copyfile(bblayersconf, bblayersconf + '.backup')
    def cleanup_bblayers(signum, frame):
        shutil.copyfile(bblayersconf + '.backup', bblayersconf)
        os.unlink(bblayersconf + '.backup')
    signal.signal(signal.SIGTERM, cleanup_bblayers)
    signal.signal(signal.SIGINT, cleanup_bblayers)

    td = {}
    results = collections.OrderedDict()
    results_status = collections.OrderedDict()

    logger.info('')
    logger.info('Getting initial bitbake variables ...')
    td['bbvars'] = get_bb_vars()
    logger.info('Getting initial signatures ...')
    td['builddir'] = builddir
    td['sigs'] = get_signatures(td['builddir'])
    logger.info('')

    layers_tested = 0
    for layer in layers:
        if layer['type'] == LayerType.ERROR_NO_LAYER_CONF or \
                layer['type'] == LayerType.ERROR_BSP_DISTRO:
            continue

        shutil.copyfile(bblayersconf + '.backup', bblayersconf)

        if not add_layer(bblayersconf, layer, layers, logger):
            results[layer['name']] = None
            results_status[layer['name']] = 'SKIPPED (Missing dependencies)'
            layers_tested = layers_tested + 1
            continue

        result = test_layer_compatibility(td, layer)
        results[layer['name']] = result
        results_status[layer['name']] = 'PASS' if results[layer['name']].wasSuccessful() else 'FAIL'
        layers_tested = layers_tested + 1

    if layers_tested:
        logger.info('')
        logger.info('Summary of results:')
        logger.info('')
        for layer_name in results_status:
            logger.info('%s ... %s' % (layer_name, results_status[layer_name]))

    cleanup_bblayers(None, None)

    return 0

if __name__ == '__main__':
    try:
        ret =  main()
    except Exception:
        ret = 1
        import traceback
        traceback.print_exc()
    sys.exit(ret)
