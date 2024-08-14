# Use node 22-alpine as base image for build stage
FROM node:22-alpine AS build
WORKDIR /app

# Install pnpm
ENV PNPM_HOME="/pnpm"
ENV PATH="$PNPM_HOME:$PATH"
RUN corepack enable

# copy only package.json to install dependencies
COPY package.json pnpm-lock.yaml /app/
RUN --mount=type=cache,id=pnpm,target=/pnpm/store pnpm install

# copy all remaining source to /app
COPY . /app
# build the frontend
RUN pnpm build


# Use nginx as base image for serving the frontend
FROM nginx:alpine-slim
# copy the build output from the build image to the nginx directory
COPY --from=build /app/dist /usr/share/nginx/html
# expose port 80
EXPOSE 80
# start nginx
CMD ["nginx", "-g", "daemon off;"]
