#!/bin/bash

# Build and copy project to Wrapper_MSDOS static folder

# Variables
dist_dir="./dist"
wrapper_dir="../Wrapper_MSDOS"
static_dir="$wrapper_dir/src/main/resources/static"

# Script
npm run build

# If compilation returns exit code = 0 then copy dist to wrapper's static directory
if [ $? -eq 0 ]; then
    rm -rf $static_dir/*
    cp -r $dist_dir/* $static_dir   
fi