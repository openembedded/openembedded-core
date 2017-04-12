# Yocto Project compatibility layer tool
#
# Copyright (C) 2017 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os
import subprocess
from enum import Enum

import bb.tinfoil

class LayerType(Enum):
    BSP = 0
    DISTRO = 1
    SOFTWARE = 2
    ERROR_NO_LAYER_CONF = 98
    ERROR_BSP_DISTRO = 99

def _get_configurations(path):
    configs = []

    for f in os.listdir(path):
        file_path = os.path.join(path, f)
        if os.path.isfile(file_path) and f.endswith('.conf'):
            configs.append(f[:-5]) # strip .conf
    return configs

def _get_layer_collections(layer_path, lconf=None, data=None):
    import bb.parse
    import bb.data

    if lconf is None:
        lconf = os.path.join(layer_path, 'conf', 'layer.conf')

    if data is None:
        ldata = bb.data.init()
        bb.parse.init_parser(ldata)
    else:
        ldata = data.createCopy()

    ldata.setVar('LAYERDIR', layer_path)
    try:
        ldata = bb.parse.handle(lconf, ldata, include=True)
    except BaseException as exc:
        raise LayerError(exc)
    ldata.expandVarref('LAYERDIR')

    collections = (ldata.getVar('BBFILE_COLLECTIONS', True) or '').split()
    if not collections:
        name = os.path.basename(layer_path)
        collections = [name]

    collections = {c: {} for c in collections}
    for name in collections:
        priority = ldata.getVar('BBFILE_PRIORITY_%s' % name, True)
        pattern = ldata.getVar('BBFILE_PATTERN_%s' % name, True)
        depends = ldata.getVar('LAYERDEPENDS_%s' % name, True)
        collections[name]['priority'] = priority
        collections[name]['pattern'] = pattern
        collections[name]['depends'] = depends

    return collections

def _detect_layer(layer_path):
    """
        Scans layer directory to detect what type of layer
        is BSP, Distro or Software.

        Returns a dictionary with layer name, type and path.
    """

    layer = {}
    layer_name = os.path.basename(layer_path)

    layer['name'] = layer_name
    layer['path'] = layer_path
    layer['conf'] = {}

    if not os.path.isfile(os.path.join(layer_path, 'conf', 'layer.conf')):
        layer['type'] = LayerType.ERROR_NO_LAYER_CONF
        return layer

    machine_conf = os.path.join(layer_path, 'conf', 'machine')
    distro_conf = os.path.join(layer_path, 'conf', 'distro')

    is_bsp = False
    is_distro = False

    if os.path.isdir(machine_conf):
        machines = _get_configurations(machine_conf)
        if machines:
            is_bsp = True

    if os.path.isdir(distro_conf):
        distros = _get_configurations(distro_conf)
        if distros:
            is_distro = True

    if is_bsp and is_distro:
        layer['type'] = LayerType.ERROR_BSP_DISTRO
    elif is_bsp:
        layer['type'] = LayerType.BSP
        layer['conf']['machines'] = machines
    elif is_distro:
        layer['type'] = LayerType.DISTRO
        layer['conf']['distros'] = distros
    else:
        layer['type'] = LayerType.SOFTWARE

    layer['collections'] = _get_layer_collections(layer['path'])

    return layer

def detect_layers(layer_directories, no_auto):
    layers = []

    for directory in layer_directories:
        directory = os.path.realpath(directory)
        if directory[-1] == '/':
            directory = directory[0:-1]

        if no_auto:
            conf_dir = os.path.join(directory, 'conf')
            if os.path.isdir(conf_dir):
                layer = _detect_layer(directory)
                if layer:
                    layers.append(layer)
        else:
            for root, dirs, files in os.walk(directory):
                dir_name = os.path.basename(root)
                conf_dir = os.path.join(root, 'conf')
                if os.path.isdir(conf_dir):
                    layer = _detect_layer(root)
                    if layer:
                        layers.append(layer)

    return layers

def _find_layer_depends(depend, layers):
    for layer in layers:
        for collection in layer['collections']:
            if depend == collection:
                return layer
    return None

def add_layer_dependencies(bblayersconf, layer, layers, logger):
    def recurse_dependencies(depends, layer, layers, logger, ret = []):
        logger.debug('Processing dependencies %s for layer %s.' % \
                    (depends, layer['name']))

        for depend in depends.split():
            # core (oe-core) is suppose to be provided
            if depend == 'core':
                continue

            layer_depend = _find_layer_depends(depend, layers)
            if not layer_depend:
                logger.error('Layer %s depends on %s and isn\'t found.' % \
                        (layer['name'], depend))
                ret = None
                continue

            # We keep processing, even if ret is None, this allows us to report
            # multiple errors at once
            if ret is not None and layer_depend not in ret:
                ret.append(layer_depend)

            # Recursively process...
            if 'collections' not in layer_depend:
                continue

            for collection in layer_depend['collections']:
                collect_deps = layer_depend['collections'][collection]['depends']
                if not collect_deps:
                    continue
                ret = recurse_dependencies(collect_deps, layer_depend, layers, logger, ret)

        return ret

    layer_depends = []
    for collection in layer['collections']:
        depends = layer['collections'][collection]['depends']
        if not depends:
            continue

        layer_depends = recurse_dependencies(depends, layer, layers, logger, layer_depends)

    # Note: [] (empty) is allowed, None is not!
    if layer_depends is None:
        return False
    else:
        for layer_depend in layer_depends:
            logger.info('Adding layer dependency %s' % layer_depend['name'])
            with open(bblayersconf, 'a+') as f:
                f.write("\nBBLAYERS += \"%s\"\n" % layer_depend['path'])
    return True

def add_layer(bblayersconf, layer, layers, logger):
    logger.info('Adding layer %s' % layer['name'])
    with open(bblayersconf, 'a+') as f:
        f.write("\nBBLAYERS += \"%s\"\n" % layer['path'])

    return True

def check_command(error_msg, cmd):
    '''
    Run a command under a shell, capture stdout and stderr in a single stream,
    throw an error when command returns non-zero exit code. Returns the output.
    '''

    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    output, _ = p.communicate()
    if p.returncode:
        msg = "%s\nCommand: %s\nOutput:\n%s" % (error_msg, cmd, output.decode('utf-8'))
        raise RuntimeError(msg)
    return output

def get_signatures(builddir, failsafe=False, machine=None):
    import re

    # some recipes needs to be excluded like meta-world-pkgdata
    # because a layer can add recipes to a world build so signature
    # will be change
    exclude_recipes = ('meta-world-pkgdata',)

    sigs = {}
    tune2tasks = {}

    cmd = ''
    if machine:
        cmd += 'MACHINE=%s ' % machine
    cmd += 'bitbake '
    if failsafe:
        cmd += '-k '
    cmd += '-S none world'
    sigs_file = os.path.join(builddir, 'locked-sigs.inc')
    if os.path.exists(sigs_file):
        os.unlink(sigs_file)
    try:
        check_command('Generating signatures failed. This might be due to some parse error and/or general layer incompatibilities.',
                      cmd)
    except RuntimeError as ex:
        if failsafe and os.path.exists(sigs_file):
            # Ignore the error here. Most likely some recipes active
            # in a world build lack some dependencies. There is a
            # separate test_machine_world_build which exposes the
            # failure.
            pass
        else:
            raise

    sig_regex = re.compile("^(?P<task>.*:.*):(?P<hash>.*) .$")
    tune_regex = re.compile("(^|\s)SIGGEN_LOCKEDSIGS_t-(?P<tune>\S*)\s*=\s*")
    current_tune = None
    with open(sigs_file, 'r') as f:
        for line in f.readlines():
            line = line.strip()
            t = tune_regex.search(line)
            if t:
                current_tune = t.group('tune')
            s = sig_regex.match(line)
            if s:
                exclude = False
                for er in exclude_recipes:
                    (recipe, task) = s.group('task').split(':')
                    if er == recipe:
                        exclude = True
                        break
                if exclude:
                    continue

                sigs[s.group('task')] = s.group('hash')
                tune2tasks.setdefault(current_tune, []).append(s.group('task'))

    if not sigs:
        raise RuntimeError('Can\'t load signatures from %s' % sigs_file)

    return (sigs, tune2tasks)

def get_depgraph(targets=['world']):
    '''
    Returns the dependency graph for the given target(s).
    The dependency graph is taken directly from DepTreeEvent.
    '''
    depgraph = None
    with bb.tinfoil.Tinfoil() as tinfoil:
        tinfoil.prepare(config_only=False)
        tinfoil.set_event_mask(['bb.event.NoProvider', 'bb.event.DepTreeGenerated', 'bb.command.CommandCompleted'])
        if not tinfoil.run_command('generateDepTreeEvent', targets, 'do_build'):
            raise RuntimeError('starting generateDepTreeEvent failed')
        while True:
            event = tinfoil.wait_event(timeout=1000)
            if event:
                if isinstance(event, bb.command.CommandFailed):
                    raise RuntimeError('Generating dependency information failed: %s' % event.error)
                elif isinstance(event, bb.command.CommandCompleted):
                    break
                elif isinstance(event, bb.event.NoProvider):
                    if event._reasons:
                        raise RuntimeError('Nothing provides %s: %s' % (event._item, event._reasons))
                    else:
                        raise RuntimeError('Nothing provides %s.' % (event._item))
                elif isinstance(event, bb.event.DepTreeGenerated):
                    depgraph = event._depgraph

    if depgraph is None:
        raise RuntimeError('Could not retrieve the depgraph.')
    return depgraph
